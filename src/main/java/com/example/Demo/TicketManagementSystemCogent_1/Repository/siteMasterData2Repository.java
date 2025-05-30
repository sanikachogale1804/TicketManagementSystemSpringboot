package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.SiteMasterData2;

@CrossOrigin
@RestResource(path = "siteMasterData2")
public interface siteMasterData2Repository extends JpaRepository<SiteMasterData2, Long>{
	
	 Optional<SiteMasterData2> findBySiteId(String siteId);

}
