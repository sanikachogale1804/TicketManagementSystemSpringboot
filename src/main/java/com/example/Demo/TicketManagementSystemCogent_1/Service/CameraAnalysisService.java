package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CameraAnalysisService {

    private final CameraReportRepository cameraReportRepository;

    public void analyzeAndSave(String basePath) {
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new IllegalArgumentException("Base path is invalid: " + basePath);
        }

        // 🔍 Get disk space info once (since it's disk-wide)
        Map<String, Double> storageInfo = getStorageInfo(basePath);
        double totalSpaceGB = storageInfo.getOrDefault("totalSpaceGB", 0.0);
        double usedSpaceGB = storageInfo.getOrDefault("usedSpaceGB", 0.0);
        double freeSpaceGB = storageInfo.getOrDefault("freeSpaceGB", 0.0);

        for (File cameraFolder : Objects.requireNonNull(baseDir.listFiles(File::isDirectory))) {
            String cameraId = cameraFolder.getName();

            File[] dateFolders = cameraFolder.listFiles(File::isDirectory);
            if (dateFolders == null || dateFolders.length == 0) continue;

            List<LocalDate> dates = new ArrayList<>();
            for (File dateFolder : dateFolders) {
                try {
                    LocalDate date = LocalDate.parse(dateFolder.getName()); // Assuming folder names are yyyy-MM-dd
                    dates.add(date);
                } catch (Exception e) {
                    System.out.println("Skipping invalid date folder: " + dateFolder.getName());
                }
            }

            if (dates.isEmpty()) continue;

            dates.sort(Comparator.naturalOrder());

            long totalBytes = calculateFolderSize(cameraFolder);
            double storageUsedGB = totalBytes / (1024.0 * 1024 * 1024);

            CameraReport report = CameraReport.builder()
                    .cameraId(cameraId)
                    .startDate(dates.get(0).toString())
                    .endDate(dates.get(dates.size() - 1).toString())
                    .recordingDays(dates.size())
                    .storageUsedGB(storageUsedGB)
                    .dateIssue(0) // Placeholder
                    .totalSpaceGB(totalSpaceGB)
                    .usedSpaceGB(usedSpaceGB)
                    .freeSpaceGB(freeSpaceGB)
                    .build();

            System.out.println("Saving report for " + cameraId + ": " + report);
            cameraReportRepository.save(report);
        }
    }

    public Map<String, Double> getStorageInfo(String path) {
        File root = new File(path);
        long total = root.getTotalSpace();
        long free = root.getFreeSpace();
        long used = total - free;

        Map<String, Double> storage = new HashMap<>();
        storage.put("totalSpaceGB", total / (1024.0 * 1024 * 1024));
        storage.put("usedSpaceGB", used / (1024.0 * 1024 * 1024));
        storage.put("freeSpaceGB", free / (1024.0 * 1024 * 1024));

        return storage;
    }

    private long calculateFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += calculateFolderSize(file);
                }
            }
        }
        return length;
    }
}
