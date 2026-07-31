package com.airline.reservation.dto.stripe;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for processing Stripe webhook events.
 * Contains the raw webhook payload and its signature for verification.
 */
public class StripeWebhookRequestDto {

    @NotBlank(message = "Webhook payload must not be blank")
    private String payload;

    @NotBlank(message = "Stripe-Signature header must not be blank")
    private String signature;

    // Constructors

    public StripeWebhookRequestDto() {
    }

    public StripeWebhookRequestDto(String payload, String signature) {
        this.payload = payload;
        this.signature = signature;
    }

    // Getters and Setters

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
