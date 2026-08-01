package com.airline.reservation.service.impl;

import com.airline.reservation.event.EventType;
import com.airline.reservation.dto.common.InngestEventRequestDto;
import com.airline.reservation.dto.common.InngestEventResponseDto;
import com.airline.reservation.service.InngestService;
import com.inngest.Inngest;
import com.inngest.InngestEvent;
import com.inngest.SendEventsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service implementation for publishing airline events to Inngest.
 * Handles event mapping and communication with the Inngest SDK.
 */
@Service
@RequiredArgsConstructor
public class InngestServiceImpl implements InngestService {

    private final Inngest inngestClient;

    // Maps application events to their corresponding Inngest event names.

    private static final Map<EventType, String> EVENT_NAME_REGISTRY = Map.of(
            EventType.BOOKING_CONFIRMED,       "airline/booking.confirmed",
            EventType.PAYMENT_SUCCESSFUL,      "airline/payment.successful",
            EventType.FLIGHT_REMINDER,         "airline/flight.reminder",
            EventType.TICKET_GENERATED,        "airline/ticket.generated",
            EventType.TICKET_EMAIL_REQUESTED,  "airline/ticket.email.requested",
            EventType.PASSENGER_NOTIFICATION,  "airline/passenger.notification"
    );

    /**
     * Publishes an event to Inngest and returns the publishing result.
     */
    @Override
    public InngestEventResponseDto publishEvent(InngestEventRequestDto request) {
        // Resolve the Inngest event name from the event type.
        String resolvedEventName = resolveEventName(request.eventType());
        // Build the event payload.
        Map<String, Object> eventData = buildEventData(request);
        // Create the Inngest event.
        InngestEvent event = new InngestEvent(resolvedEventName, eventData);
        // Publish the event to Inngest.
        SendEventsResponse response = inngestClient.send(event);
        String[] publishedIds = response.getIds();
        // Extract the generated event ID.
        String eventId = (publishedIds != null && publishedIds.length > 0)
                ? publishedIds[0]
                : null;

        return new InngestEventResponseDto(
                true,
                "Event published successfully",
                eventId,
                LocalDateTime.now()
        );
    }

    /**
     * Resolves the Inngest event name for the given event type.
     */
    private String resolveEventName(EventType eventType) {
        String eventName = EVENT_NAME_REGISTRY.get(eventType);
        if (eventName == null) {
            throw new IllegalArgumentException(
                    "No Inngest event name registered for event type: " + eventType
            );
        }
        return eventName;
    }

    /**
     * Builds the event payload from the request data.
     */
    private Map<String, Object> buildEventData(InngestEventRequestDto request) {

        Map<String, Object> data = new LinkedHashMap<>();

        if (request.bookingId() != null) data.put("bookingId", request.bookingId());
        if (request.paymentId() != null) data.put("paymentId", request.paymentId());
        if (request.ticketId()  != null) data.put("ticketId",  request.ticketId());
        if (request.userId()    != null) data.put("userId",    request.userId());
        if (request.metadata()  != null) data.putAll(request.metadata());

        return data;
    }
}
