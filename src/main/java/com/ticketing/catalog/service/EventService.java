package com.ticketing.catalog.service;

import com.ticketing.catalog.dto.CreateEventRequest;
import com.ticketing.catalog.dto.EventResponse;
import com.ticketing.catalog.dto.VenueResponse;
import com.ticketing.catalog.entity.Event;
import com.ticketing.catalog.entity.EventStatus;
import com.ticketing.catalog.entity.EventType;
import com.ticketing.catalog.entity.Venue;
import com.ticketing.catalog.exception.NotFoundException;
import com.ticketing.catalog.repository.EventRepository;
import com.ticketing.catalog.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    
    public List<EventResponse> searchEvents(String city, String type, String status) {
        Specification<Event> spec = Specification.where(null);
        
        if (type != null && !type.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.upper(root.get("type").as(String.class)), type.toUpperCase()));
        }
        
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.upper(root.get("status").as(String.class)), status.toUpperCase()));
        }
        
        List<Event> events = eventRepository.findAll(spec);
        
        // Filter by city if provided (venue-based filtering)
        if (city != null && !city.isEmpty()) {
            String cityLower = city.toLowerCase();
            events = events.stream()
                .filter(event -> {
                    Venue venue = venueRepository.findById(event.getVenueId()).orElse(null);
                    return venue != null && venue.getCity().toLowerCase().contains(cityLower);
                })
                .collect(Collectors.toList());
        }
        
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        return mapToResponse(event);
    }
    
    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        // Verify venue exists
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new NotFoundException("Venue not found"));
        
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setType(EventType.valueOf(request.getType().toUpperCase()));
        event.setVenueId(request.getVenueId());
        event.setEventDate(request.getEventDate());
        event.setBasePrice(request.getBasePrice());
        event.setStatus(EventStatus.ON_SALE);
        
        event = eventRepository.save(event);
        return mapToResponse(event);
    }
    
    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setType(event.getType().name());
        response.setVenueId(event.getVenueId());
        response.setEventDate(event.getEventDate());
        response.setBasePrice(event.getBasePrice());
        response.setStatus(event.getStatus().name());
        
        // Fetch venue details
        venueRepository.findById(event.getVenueId()).ifPresent(venue -> {
            VenueResponse venueResponse = new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getState(),
                venue.getAddress(),
                venue.getCapacity()
            );
            response.setVenue(venueResponse);
        });
        
        return response;
    }
}

