package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CameraReport {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @Column(name = "camera_id")
	 private String cameraId;

	 @Column(name = "start_date")
	 private String startDate;

	 @Column(name = "end_date")
	 private String endDate;

	 @Column(name = "recording_days")
	 private int recordingDays;

	 @Column(name = "storage_used_gb")
	 private double storageUsedGB;

	 @Column(name = "date_issue")
	 private int dateIssue;
	
	 @Column(name = "total_space_gb")
	 private Double totalSpaceGB;

	 @Column(name = "used_space_gb")
	 private Double usedSpaceGB;

	 @Column(name = "free_space_gb")
	 private Double freeSpaceGB;
	 
	 @ManyToOne
	 @JoinColumn(name = "site_master_id",referencedColumnName = "id") // This column will be the foreign key to SiteMasterData
	 private SiteMasterData2 site;
	 
	 
}
