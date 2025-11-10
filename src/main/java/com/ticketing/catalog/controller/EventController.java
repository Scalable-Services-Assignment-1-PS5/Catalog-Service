package com.ticketing.catalog.controller;

import com.ticketing.catalog.dto.CreateEventRequest;
import com.ticketing.catalog.dto.EventResponse;
import com.ticketing.catalog.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Event catalog management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class EventController {
    
    private final EventService eventService;
    
    @GetMapping
    @Operation(summary = "Search events", description = "Search and filter events by city, type, and status")
    public ResponseEntity<List<EventResponse>> searchEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(eventService.searchEvents(city, type, status));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Retrieve event details by ID")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
    
    @PostMapping
    @Operation(summary = "Create event", description = "Create a new event (admin only)")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }
}

