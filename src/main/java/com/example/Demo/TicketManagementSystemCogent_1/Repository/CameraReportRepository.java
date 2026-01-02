package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.CameraReport;

import jakarta.transaction.Transactional;

@RepositoryRestResource(path = "camera-reports")
@CrossOrigin
public interface CameraReportRepository extends JpaRepository<CameraReport, Long>{
	
	void deleteByCameraId(String cameraId);

	CameraReport findByCameraId(String cameraId);
	
	 Optional<CameraReport> findByCameraIdAndMediaType(String cameraId, String mediaType);
	 
	 @Transactional
	 @Modifying
	 @Query("DELETE FROM CameraReport c WHERE c.createdAt = :date")
	 int deleteByCreatedAt(@Param("date") LocalDate date);

}
