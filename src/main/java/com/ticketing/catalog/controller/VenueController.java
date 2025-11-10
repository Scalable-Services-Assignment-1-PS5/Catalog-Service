package com.ticketing.catalog.controller;

import com.ticketing.catalog.dto.VenueResponse;
import com.ticketing.catalog.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "Venue management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class VenueController {
    
    private final VenueService venueService;
    
    @GetMapping
    @Operation(summary = "Get all venues", description = "Retrieve list of all venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get venue by ID", description = "Retrieve venue details by ID")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getVenueById(id));
    }
}

