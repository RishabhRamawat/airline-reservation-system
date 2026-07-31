package com.airline.reservation.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO used to verify a completed Stripe Checkout Session.
 */
@Data
public class PaymentVerificationRequestDto {

    @NotBlank(message = "Stripe session ID is required.")
    private String sessionId;

}
