package com.example.Demo.TicketManagementSystemCogent_1.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class CameraAnalysisService {

	 private final CameraReportRepository cameraReportRepository;

	    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M_d_yyyy");
	    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	    public void analyzeAndSave(String basePath) throws InterruptedException, ExecutionException {
	        File baseDir = new File(basePath);
	        if (!baseDir.exists() || !baseDir.isDirectory()) {
	            throw new IllegalArgumentException("Base path does not exist or is not a directory: " + basePath);
	        }

	        List<File> mediaDirs = new ArrayList<>();
	        findMediaFoldersRecursively(baseDir, mediaDirs);

	        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
	        List<Future<CameraReport>> futures = new ArrayList<>();

	        for (File mediaDir : mediaDirs) {
	            futures.add(executor.submit(() -> processCameraFolder(mediaDir)));
	        }

	        Map<String, CameraReport> combinedReports = new HashMap<>();

	        for (Future<CameraReport> future : futures) {
	            CameraReport report = future.get();
	            if (report == null) continue;

	            combinedReports.merge(report.getCameraId(), report, (existing, incoming) -> {
	                existing.setStorageUsedGB(existing.getStorageUsedGB() + incoming.getStorageUsedGB());

	                if (incoming.getStartDate() != null) {
	                    LocalDate existingStart = existing.getStartDate() != null ? LocalDate.parse(existing.getStartDate()) : null;
	                    LocalDate incomingStart = LocalDate.parse(incoming.getStartDate());
	                    if (existingStart == null || incomingStart.isBefore(existingStart)) {
	                        existing.setStartDate(incoming.getStartDate());
	                    }
	                }

	                if (incoming.getEndDate() != null) {
	                    LocalDate existingEnd = existing.getEndDate() != null ? LocalDate.parse(existing.getEndDate()) : null;
	                    LocalDate incomingEnd = LocalDate.parse(incoming.getEndDate());
	                    if (existingEnd == null || incomingEnd.isAfter(existingEnd)) {
	                        existing.setEndDate(incoming.getEndDate());
	                    }
	                }

	                // Recalculate recording days
	                if (existing.getStartDate() != null && existing.getEndDate() != null) {
	                    LocalDate s = LocalDate.parse(existing.getStartDate());
	                    LocalDate e = LocalDate.parse(existing.getEndDate());
	                    existing.setRecordingDays((int) (e.toEpochDay() - s.toEpochDay() + 1));
	                }

	                return existing;
	            });
	        }

	        executor.shutdown();

	        // Batch save all reports
	        cameraReportRepository.saveAll(new ArrayList<>(combinedReports.values()));
	        System.out.println("Saved " + combinedReports.size() + " camera reports.");
	    }

	    private CameraReport processCameraFolder(File mediaDir) {
	        try {
	            String cameraId = mediaDir.getParentFile().getName();
	            String mediaType = mediaDir.getName().equalsIgnoreCase("picture") ? "picture" : "video";

	            double storageUsedGB = calculateFolderSizeGB(mediaDir);
	            LocalDate startDate = null;
	            LocalDate endDate = null;

	            File[] contents = mediaDir.listFiles();
	            if (contents != null) {
	                for (File fileOrFolder : contents) {
	                    if (fileOrFolder.isDirectory()) {
	                        try {
	                            LocalDate date = LocalDate.parse(fileOrFolder.getName(), DATE_FORMAT);
	                            if (startDate == null || date.isBefore(startDate)) startDate = date;
	                            if (endDate == null || date.isAfter(endDate)) endDate = date;
	                        } catch (Exception ignored) {}
	                    }
	                }
	            }

	            int recordingDays = (startDate != null && endDate != null)
	                    ? (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1)
	                    : 0;

	            double totalSpaceGB = 0, usedSpaceGB = 0, freeSpaceGB = 0;
	            try {
	                Path path = mediaDir.toPath();
	                FileStore store = Files.getFileStore(path);
	                totalSpaceGB = bytesToGB(store.getTotalSpace());
	                freeSpaceGB = bytesToGB(store.getUsableSpace());
	                usedSpaceGB = totalSpaceGB - freeSpaceGB;
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	            return CameraReport.builder()
	                    .cameraId(cameraId)
	                    .startDate(startDate != null ? startDate.toString() : null)
	                    .endDate(endDate != null ? endDate.toString() : null)
	                    .recordingDays(recordingDays)
	                    .storageUsedGB(storageUsedGB)
	                    .dateIssue(0)
	                    .totalSpaceGB(totalSpaceGB)
	                    .usedSpaceGB(usedSpaceGB)
	                    .freeSpaceGB(freeSpaceGB)
	                    .mediaType("combined")
	                    .build();

	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    private void findMediaFoldersRecursively(File folder, List<File> mediaDirs) {
	        File[] files = folder.listFiles(File::isDirectory);
	        if (files == null) return;

	        for (File subFolder : files) {
	            if ("video".equalsIgnoreCase(subFolder.getName()) || "picture".equalsIgnoreCase(subFolder.getName())) {
	                mediaDirs.add(subFolder);
	            } else {
	                findMediaFoldersRecursively(subFolder, mediaDirs);
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
	                length += file.isFile() ? file.length() : calculateFolderSizeBytes(file);
	            }
	        }
	        return length;
	    }
	}