package com.example.Demo.TicketManagementSystemCogent_1.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SiteMasterData2 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String siteId;
	
	private String iasspName;
	
	private String state;
	
	private String district;
	
	@OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
	private List<CameraReport> cameraReports;
	
}
