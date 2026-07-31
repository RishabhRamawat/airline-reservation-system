package com.airline.reservation.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutSessionRequestDto {

    @NotNull(message = "Booking ID is required.")
    private Long bookingId;

}
