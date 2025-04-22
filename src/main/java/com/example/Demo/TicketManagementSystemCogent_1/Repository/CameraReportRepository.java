package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;

@RestResource(path = "cameraRestRepository")
@CrossOrigin
public interface CameraReportRepository extends JpaRepository<CameraReport, Long>{
	
	void deleteByCameraId(String cameraId);


}
