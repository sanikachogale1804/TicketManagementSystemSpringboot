package com.example.Demo.TicketManagementSystemCogent_1.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CameraAnalysisService {

    private final CameraReportRepository cameraReportRepository;
    private static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void analyzeAndSave(String basePath) {
        File base = new File(basePath);
        System.out.println("📂 Base path: " + basePath);

        if (!base.isDirectory()) {
            throw new IllegalArgumentException("❌ Invalid base path: " + basePath);
        }

        Date now = new Date();
        File[] cameraFolders = base.listFiles();

        if (cameraFolders == null || cameraFolders.length == 0) {
            System.out.println("⚠️ No folders found under base path.");
            return;
        }

        for (File cameraFolder : cameraFolders) {
            if (!cameraFolder.isDirectory()) continue;

            System.out.println("\n🔍 Processing camera folder: " + cameraFolder.getName());

            File[] dateFolders = cameraFolder.listFiles();
            if (dateFolders == null || dateFolders.length == 0) {
                System.out.println("⚠️ No date folders in " + cameraFolder.getPath());
                continue;
            }

            List<String> validDates = new ArrayList<>();
            double totalSize = 0;

            for (File dateFolder : dateFolders) {
                if (dateFolder.isDirectory() && dateFolder.getName().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    File[] files = dateFolder.listFiles(File::isFile);

                    boolean hasNonEmptyFile = false;
                    if (files != null) {
                        for (File f : files) {
                            if (f.length() > 0) {
                                hasNonEmptyFile = true;
                                break;
                            }
                        }
                    }

                    if (hasNonEmptyFile) {
                        validDates.add(dateFolder.getName());
                        double folderSize = folderSizeInGB(dateFolder);
                        totalSize += folderSize;
                        System.out.printf("✅ Valid: %s (%.2f GB)\n", dateFolder.getName(), folderSize);
                    } else if (files != null && files.length > 0) {
                        System.out.println("❌ Skipped (only empty files): " + dateFolder.getName());
                    } else {
                        System.out.println("❌ Skipped (no files): " + dateFolder.getName());
                    }
                }
            }

            if (!validDates.isEmpty()) {
                validDates.sort(Comparator.comparing(d -> {
                    try {
                        return inputFormat.parse(d);
                    } catch (Exception e) {
                        return new Date(0);
                    }
                }));

                Date start = parseDate(validDates.get(0));
                Date end = parseDate(validDates.get(validDates.size() - 1));
                boolean dateIssue = now.getTime() - end.getTime() > 7L * 24 * 60 * 60 * 1000;

                CameraReport report = CameraReport.builder()
                        .cameraId(cameraFolder.getName())
                        .startDate(outputFormat.format(start))
                        .endDate(outputFormat.format(end))
                        .recordingDays(validDates.size())
                        .storageUsedGB(totalSize)
                        .dateIssue(dateIssue ? 1 : 0)
                        .build();

                cameraReportRepository.save(report);
                System.out.println("💾 Saved to DB: " + report);
            } else {
                System.out.println("⚠️ No valid date folders found in: " + cameraFolder.getName());
            }
        }
    }

    public Map<String, Double> getStorageInfo(String basePath) {
        File base = new File(basePath);
        Map<String, Double> storageInfo = new HashMap<>();

        if (!base.exists() || !base.isDirectory()) {
            throw new IllegalArgumentException("Invalid base path: " + basePath);
        }

        double totalSpaceGB = base.getTotalSpace() / (1024.0 * 1024 * 1024);
        double freeSpaceGB = base.getFreeSpace() / (1024.0 * 1024 * 1024);
        double usedSpaceGB = totalSpaceGB - freeSpaceGB;

        storageInfo.put("totalSpaceGB", totalSpaceGB);
        storageInfo.put("usedSpaceGB", usedSpaceGB);
        storageInfo.put("freeSpaceGB", freeSpaceGB);

        return storageInfo;
    }

    private double folderSizeInGB(File folder) {
        final long[] size = {0};
        try {
            Files.walk(folder.toPath()).forEach(path -> {
                if (path.toFile().isFile()) {
                    size[0] += path.toFile().length();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size[0] / (1024.0 * 1024 * 1024);
    }

    private Date parseDate(String dateStr) {
        try {
            return inputFormat.parse(dateStr);
        } catch (Exception e) {
            return new Date(0);
        }
    }
}
