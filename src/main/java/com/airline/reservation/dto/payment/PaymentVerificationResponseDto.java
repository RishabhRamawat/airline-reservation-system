package com.airline.reservation.dto.payment;

import lombok.Data;

/**
 * Response DTO returned after verifying a Stripe payment.
 */
@Data
public class PaymentVerificationResponseDto {

    private Long paymentId;
    private String paymentStatus;
    private String message;

}
