package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Service.CameraAnalysisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CameraNightService {
	
	private final CameraAnalysisService cameraAnalysisService;
	
	 @Scheduled(cron = "0 0 0 * * *")
	    public void fetchAndUpdateNightlyData() {
	        try {
	            System.out.println("Nightly camera data update started...");
	            
	            cameraAnalysisService.analyzeAndSave("Z:\\NAS~1"); 
	            
	            System.out.println("Nightly camera data update completed successfully!");
	        } catch (Exception e) {
	            System.err.println("Error during nightly camera data update: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}