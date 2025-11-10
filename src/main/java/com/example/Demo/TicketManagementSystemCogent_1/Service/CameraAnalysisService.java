package com.example.Demo.TicketManagementSystemCogent_1.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CameraAnalysisService {

    private final CameraReportRepository cameraReportRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M_d_yyyy");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * Main method to analyze cameras and save reports
     */
    public void analyzeAndSave(String basePath) {
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Base path does not exist or is not a directory: " + basePath);
        }

        List<File> videoDirs = new ArrayList<>();
        findVideoFoldersRecursively(baseDir, videoDirs);

        if (videoDirs.isEmpty()) {
            System.out.println("No 'video' folders found under: " + basePath);
            return;
        }

        for (File videoDir : videoDirs) {
            String cameraId = videoDir.getParentFile().getName(); // Parent folder name = camera ID

            File[] dateFolders = videoDir.listFiles(File::isDirectory);
            if (dateFolders == null || dateFolders.length == 0) {
                System.out.println("No date folders found for camera: " + cameraId);
                continue;
            }

            // Get start and end date
            LocalDate startDate = null;
            LocalDate endDate = null;

            for (File dateFolder : dateFolders) {
                try {
                    LocalDate date = LocalDate.parse(dateFolder.getName(), DATE_FORMAT);
                    if (startDate == null || date.isBefore(startDate)) startDate = date;
                    if (endDate == null || date.isAfter(endDate)) endDate = date;
                } catch (Exception ignored) {
                }
            }

            int recordingDays = (startDate != null && endDate != null)
                    ? (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1)
                    : 0;

            // Get storage info
            double totalSpaceGB = 0;
            double usedSpaceGB = 0;
            double freeSpaceGB = 0;

            try {
                Path path = videoDir.toPath();
                FileStore store = Files.getFileStore(path);
                totalSpaceGB = bytesToGB(store.getTotalSpace());
                freeSpaceGB = bytesToGB(store.getUsableSpace());
                usedSpaceGB = totalSpaceGB - freeSpaceGB;
            } catch (Exception e) {
                e.printStackTrace();
            }

            double storageUsedGB = calculateFolderSizeGB(videoDir);

            CameraReport report = CameraReport.builder()
                    .cameraId(cameraId)
                    .startDate(startDate != null ? startDate.toString() : null)
                    .endDate(endDate != null ? endDate.toString() : null)
                    .recordingDays(recordingDays)
                    .storageUsedGB(storageUsedGB)
                    .dateIssue(0)
                    .totalSpaceGB(totalSpaceGB)
                    .usedSpaceGB(usedSpaceGB)
                    .freeSpaceGB(freeSpaceGB)
                    .build();

            System.out.println("Saving report: " + report);
            cameraReportRepository.save(report);
        }
    }

    /**
     * Recursive method to find all "video" folders
     */
    private void findVideoFoldersRecursively(File folder, List<File> videoDirs) {
        File[] files = folder.listFiles(File::isDirectory);
        if (files == null) return;

        for (File subFolder : files) {
            if ("video".equalsIgnoreCase(subFolder.getName())) {
                videoDirs.add(subFolder);
            } else {
                findVideoFoldersRecursively(subFolder, videoDirs);
            }
        }
    }

    private double bytesToGB(long bytes) {
        return Double.parseDouble(DECIMAL_FORMAT.format(bytes / (1024.0 * 1024.0 * 1024.0)));
    }

    private double calculateFolderSizeGB(File folder) {
        return bytesToGB(calculateFolderSizeBytes(folder));
    }

    private long calculateFolderSizeBytes(File folder) {
        long length = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += calculateFolderSizeBytes(file);
                }
            }
        }
        return length;
    }
}
