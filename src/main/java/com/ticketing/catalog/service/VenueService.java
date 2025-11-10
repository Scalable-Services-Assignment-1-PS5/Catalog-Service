package com.ticketing.catalog.service;

import com.ticketing.catalog.dto.VenueResponse;
import com.ticketing.catalog.entity.Venue;
import com.ticketing.catalog.exception.NotFoundException;
import com.ticketing.catalog.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueService {
    
    private final VenueRepository venueRepository;
    
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue not found"));
        return mapToResponse(venue);
    }
    
    private VenueResponse mapToResponse(Venue venue) {
        return new VenueResponse(
            venue.getId(),
            venue.getName(),
            venue.getCity(),
            venue.getState(),
            venue.getAddress(),
            venue.getCapacity()
        );
    }
}

