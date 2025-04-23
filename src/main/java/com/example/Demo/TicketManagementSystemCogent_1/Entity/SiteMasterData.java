package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SiteMasterData {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @Column(name = "site_id", nullable = false)
	 private String siteId;

	 @Column(name = "site_live_date")
	 private String siteLiveDate;

	 @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
	 private List<CameraReport> cameraReports;

}
