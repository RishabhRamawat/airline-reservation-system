package com.airline.reservation.dto.stripe;

/**
 * Response DTO returned after creating a Stripe Checkout Session.
 * Contains the checkout URL and operation status.
 */
public class CheckoutResponse {

    private String sessionId;

    private String checkoutUrl;

    private boolean success;

    private String message;

    public CheckoutResponse() {
    }

    public CheckoutResponse(
            String sessionId,
            String checkoutUrl,
            boolean success,
            String message) {
        this.sessionId = sessionId;
        this.checkoutUrl = checkoutUrl;
        this.success = success;
        this.message = message;
    }

    /**
     * Creates a successful response.
     */
    public static CheckoutResponse success(String sessionId, String checkoutUrl) {
        return new CheckoutResponse(
                sessionId,
                checkoutUrl,
                true,
                "Checkout session created successfully");
    }

    /**
     * Creates a failure response .
     */
    public static CheckoutResponse failure(String message) {
        return new CheckoutResponse(null, null, false, message);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public void setCheckoutUrl(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
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
