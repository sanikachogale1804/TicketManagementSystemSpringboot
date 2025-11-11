package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;

@RepositoryRestResource(path = "camera-reports")
@CrossOrigin
public interface CameraReportRepository extends JpaRepository<CameraReport, Long>{
	
	void deleteByCameraId(String cameraId);

	CameraReport findByCameraId(String cameraId);
	
	 Optional<CameraReport> findByCameraIdAndMediaType(String cameraId, String mediaType);
}
