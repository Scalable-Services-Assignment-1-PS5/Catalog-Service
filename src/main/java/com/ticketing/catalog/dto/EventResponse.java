package com.ticketing.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private Long venueId;
    private LocalDateTime eventDate;
    private BigDecimal basePrice;
    private String status;
    private VenueResponse venue;
}

