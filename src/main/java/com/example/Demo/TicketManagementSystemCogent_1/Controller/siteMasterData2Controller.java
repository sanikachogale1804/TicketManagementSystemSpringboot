package com.example.Demo.TicketManagementSystemCogent_1.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.SiteMasterData2;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.siteMasterData2Repository;

@RestController
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "https://rainbow-kataifi-7acd83.netlify.app/",//local
	    "https://cogentmobileapp.in:8443",//VM,
	    "http://45.115.186.228:3000"
 	})  // Allow requests from React frontend
@RequestMapping("/siteMasterData2")
public class siteMasterData2Controller {

    @Autowired
    private siteMasterData2Repository siteRepository;

    // GET method to get site data by id with HATEOAS links
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SiteMasterData2>> getSiteData(@PathVariable Long id) {
        Optional<SiteMasterData2> siteDataOpt = siteRepository.findById(id);
        if (siteDataOpt.isPresent()) {
            SiteMasterData2 siteData = siteDataOpt.get();
            EntityModel<SiteMasterData2> resource = EntityModel.of(siteData,
                    linkTo(methodOn(siteMasterData2Controller.class).getSiteData(id)).withSelfRel(),
                    linkTo(methodOn(siteMasterData2Controller.class).getAllSites()).withRel("allSites"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET method to get all sites with HATEOAS links
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SiteMasterData2>>> getAllSites() {
        List<EntityModel<SiteMasterData2>> sites = siteRepository.findAll().stream()
                .map(site -> EntityModel.of(site,
                        linkTo(methodOn(siteMasterData2Controller.class).getSiteData(site.getId())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SiteMasterData2>> collectionModel = CollectionModel.of(sites,
                linkTo(methodOn(siteMasterData2Controller.class).getAllSites()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // POST method to add multiple sites (no links needed here)
    @PostMapping
    public ResponseEntity<List<SiteMasterData2>> addSites(@RequestBody List<SiteMasterData2> sites) {
        List<SiteMasterData2> savedSites = siteRepository.saveAll(sites);
        return ResponseEntity.ok(savedSites);
    }
    
}
