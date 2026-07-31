package com.airline.reservation.service;

import com.airline.reservation.dto.payment.CheckoutSessionRequestDto;
import com.airline.reservation.dto.payment.CheckoutSessionResponseDto;
import com.airline.reservation.dto.payment.PaymentDetailsResponseDto;
import com.airline.reservation.dto.payment.PaymentVerificationRequestDto;
import com.airline.reservation.dto.payment.PaymentVerificationResponseDto;

public interface PaymentService {

    CheckoutSessionResponseDto createCheckoutSession(
            CheckoutSessionRequestDto request);

    PaymentVerificationResponseDto verifyPayment(
            PaymentVerificationRequestDto request);

    PaymentDetailsResponseDto getPaymentDetails(Long paymentId);

}
