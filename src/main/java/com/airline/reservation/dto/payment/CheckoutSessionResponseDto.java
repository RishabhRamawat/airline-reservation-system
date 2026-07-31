package com.airline.reservation.dto.payment;

import lombok.Data;

/**
 * Response DTO returned after creating a Stripe Checkout Session.
 */
@Data
public class CheckoutSessionResponseDto {

    private String sessionId;
    private String checkoutUrl;

}
