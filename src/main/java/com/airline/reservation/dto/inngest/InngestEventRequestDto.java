package com.airline.reservation.dto.inngest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

/**
 * Request DTO for publishing an event to Inngest.
 */
public record InngestEventRequestDto(

        // Type of event to publish
        @NotNull(message = "Event type must not be null")
        EventType eventType,

        // Booking identifier
        @Positive(message = "Booking ID must be positive")
        Long bookingId,

        // Payment identifier
        @Positive(message = "Payment ID must be positive")
        Long paymentId,

        // Ticket identifier
        @Positive(message = "Ticket ID must be positive")
        Long ticketId,

        // Passenger identifier
        @Positive(message = "User ID must be positive")
        Long userId,

        // Additional event-specific data
        Map<String, Object> metadata
) {}