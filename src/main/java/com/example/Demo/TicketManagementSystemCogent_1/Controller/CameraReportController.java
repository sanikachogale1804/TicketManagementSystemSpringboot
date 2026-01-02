package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.awt.print.Pageable;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.CameraReportRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Service.CameraAnalysisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/camera-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://127.0.0.1:3000",
	    "http://192.168.1.91:3000",
	    "http://117.250.211.51:3000"
	})
public class CameraReportController {
	
	 private final CameraReportRepository cameraReportRepository;
	 private final CameraAnalysisService cameraAnalysisService; 

//	    @GetMapping
//	    public List<CameraReport> getAllReports() {
//	        return cameraReportRepository.findAll();
//	    }
	 
	 @GetMapping("/all")
	 public List<CameraReport> getAllCameraReports() {
	     return cameraReportRepository.findAll();
	 }

	    
	 @GetMapping("/analyze")
	 public String runAnalysis() {
	     try {
	         // VM path
	         cameraAnalysisService.analyzeAndSave("D:\\NAS1");
	         return "Analysis Done!";
	     } catch (Exception e) {
	         e.printStackTrace();
	         return "Error occurred during analysis: " + e.getMessage();
	     }
	 }

	 @PostMapping("/analyze")
	    public ResponseEntity<String> saveReports(@RequestBody List<CameraReport> reports) {
	        if (reports == null || reports.isEmpty()) {
	            return ResponseEntity.badRequest().body("No data received!");
	        }
	        cameraReportRepository.saveAll(reports);
	        return ResponseEntity.ok("Saved " + reports.size() + " camera reports successfully!");
	    }
	 
	 @DeleteMapping("/delete-by-date")
	 public String deleteByDate(@RequestParam String date) {
	     LocalDate d = LocalDate.parse(date);
	     int deleted = cameraReportRepository.deleteByCreatedAt(d);
	     return "Deleted records = " + deleted;
	 }

	    
//	    @GetMapping("/storage-info")
//	    public Map<String, Double> getStorageInfo() {
//	        return cameraAnalysisService.getStorageInfo("Z:\\");
//	    }

	    
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
	    
//	    @GetMapping("/create-folders")
//	    public String createCameraFolders() {
//	        List<String> cameraIds = List.of(
//	            "IPC-S5D86998951033",
//	            "IPC-S5D86998414026",
//	            "IPC-S5D86998858134",
//	            "IPC-S5D86997189514",
//	            "IPC-S5D87069589734",
//	            "IPC-S5D86996937664",
//	            "IPC-ATPL-905145-AIPTZ",
//	            "IPC-S5D86996615661",
//	            "IPC-S5D87070913310",
//	            "IPC-ATPL-904989-AIPTZ",
//	            "IPC-ATPL-905862-AIPTZ"
//	        );
//
//	        String basePath = "Z:\\"; // 👈 Change this to your desired base path
//
//	        File baseDir = new File(basePath);
//	        if (!baseDir.exists()) baseDir.mkdirs();
//
//	        for (String id : cameraIds) {
//	            File folder = new File(baseDir, id);
//	            if (!folder.exists()) {
//	                boolean created = folder.mkdirs();
//	                System.out.println("Created: " + folder.getAbsolutePath() + " -> " + created);
//	            }
//	        }
//
//	        return "Folders created successfully!";
//	    }
	    
//	    @GetMapping("/create-date-folders")
//	    public String createDateFolders() {
//	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
//	        
//	        String start = "10/29/2024";
//	        String end = "1/10/2025";
//
//	        LocalDate startDate = LocalDate.parse(start, formatter);
//	        LocalDate endDate = LocalDate.parse(end, formatter);
//
//	        String basePath = "D:\\NAS1\\IPC-ATPL-904989-AIPTZ"; // Change as needed
//
//	        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//	            String folderName = date.toString(); // e.g., 2024-10-29
//	            File folder = new File(basePath, folderName);
//	            if (!folder.exists()) {
//	                folder.mkdirs();
//	                System.out.println("Created: " + folder.getAbsolutePath());
//	            }
//	        }
//
//	        return "Date folders created from " + startDate + " to " + endDate;
//	    }
	    
	    

}
