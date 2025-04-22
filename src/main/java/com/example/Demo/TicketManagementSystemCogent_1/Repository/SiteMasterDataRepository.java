package com.example.Demo.TicketManagementSystemCogent_1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.SiteMasterData;


@CrossOrigin
public interface SiteMasterDataRepository extends JpaRepository<SiteMasterData, Long>{
	 Optional<SiteMasterData> findBySiteId(String siteId);

}
