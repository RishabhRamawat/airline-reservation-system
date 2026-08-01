package com.airline.reservation.event;

/**
 * Supported airline events published to Inngest.
 */
public enum EventType {

    BOOKING_CONFIRMED,
    PAYMENT_SUCCESSFUL,
    FLIGHT_REMINDER,
    TICKET_GENERATED,
    TICKET_EMAIL_REQUESTED,
    PASSENGER_NOTIFICATION
}

