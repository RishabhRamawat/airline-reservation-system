package com.airline.reservation.dto.payment;

import lombok.Data;

/**
 * Response DTO containing payment details.
 */
@Data
public class PaymentDetailsResponseDto {

    private Long paymentId;
    private Long bookingId;
    private Double amount;
    private String paymentStatus;
    private String paymentMethod;

}
