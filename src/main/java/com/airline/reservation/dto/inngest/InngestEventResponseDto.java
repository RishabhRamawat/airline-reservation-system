package com.airline.reservation.dto.inngest;

import java.time.LocalDateTime;

/**
 * Response DTO returned after publishing an event to Inngest.
 */
public record InngestEventResponseDto(

        // Whether the event was published successfully
        boolean success,

        // Response message
        String message,

        // Unique event ID returned by Inngest
        String eventId,

        // Time when the event was published
        LocalDateTime timestamp
) {}