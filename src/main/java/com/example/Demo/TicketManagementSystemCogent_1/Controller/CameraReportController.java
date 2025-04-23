package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Service.CameraAnalysisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/camera-reports")
@RequiredArgsConstructor
public class CameraReportController {
	
	 private final CameraReportRepository cameraReportRepository;
	 private final CameraAnalysisService cameraAnalysisService; 

//	    @GetMapping
//	    public List<CameraReport> getAllReports() {
//	        return cameraReportRepository.findAll();
//	    }
	    
	    @GetMapping("/analyze")
	    public String runAnalysis() {
	        try {
	            cameraAnalysisService.analyzeAndSave("D:\\NAS1"); 
	            return "Analysis Done!";
	        } catch (Exception e) {
	            e.printStackTrace(); // Log the error
	            return "Error occurred during analysis: " + e.getMessage();
	        }
	    }
	    
	    @GetMapping("/storage-info")
	    public Map<String, Double> getStorageInfo() {
	        return cameraAnalysisService.getStorageInfo("D:\\NAS1");
	    }

	    
//	    @GetMapping("/test-insert")
//	    public String insertDummy() {
//	        CameraReport r = CameraReport.builder()
//	                .cameraId("TEST-CAM-001")
//	                .startDate("01/01/2024")
//	                .endDate("01/02/2024")
//	                .recordingDays(2)
//	                .storageUsedGB(1.23)
//	                .dateIssue(0)
//	                .build();
//	        cameraReportRepository.save(r);
//	        return "Inserted test report!";
//	    }
	    
	    @GetMapping("/create-folders")
	    public String createCameraFolders() {
	        List<String> cameraIds = List.of(
	            "IPC-S5D86998951033",
	            "IPC-S5D86998414026",
	            "IPC-S5D86998858134",
	            "IPC-S5D86997189514",
	            "IPC-S5D87069589734",
	            "IPC-S5D86996937664",
	            "IPC-ATPL-905145-AIPTZ",
	            "IPC-S5D86996615661",
	            "IPC-S5D87070913310",
	            "IPC-ATPL-904989-AIPTZ",
	            "IPC-ATPL-905862-AIPTZ"
	        );

	        String basePath = "D:\\NAS1"; // 👈 Change this to your desired base path

	        File baseDir = new File(basePath);
	        if (!baseDir.exists()) baseDir.mkdirs();

	        for (String id : cameraIds) {
	            File folder = new File(baseDir, id);
	            if (!folder.exists()) {
	                boolean created = folder.mkdirs();
	                System.out.println("Created: " + folder.getAbsolutePath() + " -> " + created);
	            }
	        }

	        return "Folders created successfully!";
	    }
	    
	    @GetMapping("/create-date-folders")
	    public String createDateFolders() {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
	        
	        String start = "10/29/2024";
	        String end = "1/10/2025";

	        LocalDate startDate = LocalDate.parse(start, formatter);
	        LocalDate endDate = LocalDate.parse(end, formatter);

	        String basePath = "D:\\NAS1\\IPC-ATPL-904989-AIPTZ"; // Change as needed

	        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	            String folderName = date.toString(); // e.g., 2024-10-29
	            File folder = new File(basePath, folderName);
	            if (!folder.exists()) {
	                folder.mkdirs();
	                System.out.println("Created: " + folder.getAbsolutePath());
	            }
	        }

	        return "Date folders created from " + startDate + " to " + endDate;
	    }

}
