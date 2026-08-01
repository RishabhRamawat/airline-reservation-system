package com.airline.reservation.dto.payment;

/**
 * Response DTO returned after processing a Stripe webhook event.
 * Contains the event details and processing result.
 */
public class StripeWebhookResponse {

    private String eventId;

    private String eventType;

    private String paymentIntentId;

    private String checkoutSessionId;

    private String paymentStatus;

    private boolean success;

    private String message;

    // Constructors

    public StripeWebhookResponse() {
    }

    public StripeWebhookResponse(
            String eventId,
            String eventType,
            String paymentIntentId,
            String checkoutSessionId,
            String paymentStatus,
            boolean success,
            String message) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.paymentIntentId = paymentIntentId;
        this.checkoutSessionId = checkoutSessionId;
        this.paymentStatus = paymentStatus;
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a failure response.
     */
    public static StripeWebhookResponse failure(String message) {
        return new StripeWebhookResponse(
                null, null, null, null, "FAILED", false, message);
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getCheckoutSessionId() {
        return checkoutSessionId;
    }

    public void setCheckoutSessionId(String checkoutSessionId) {
        this.checkoutSessionId = checkoutSessionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}