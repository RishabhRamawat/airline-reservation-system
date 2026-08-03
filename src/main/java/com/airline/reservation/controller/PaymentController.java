package com.airline.reservation.controller;

import com.airline.reservation.dto.payment.CheckoutSessionRequestDto;
import com.airline.reservation.dto.payment.CheckoutSessionResponseDto;
import com.airline.reservation.dto.payment.PaymentDetailsResponseDto;
import com.airline.reservation.dto.payment.PaymentVerificationRequestDto;
import com.airline.reservation.dto.payment.PaymentVerificationResponseDto;
import com.airline.reservation.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// Activated automatically when PaymentService implementation is registered.
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
@ConditionalOnBean(PaymentService.class)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout-session")
    public ResponseEntity<CheckoutSessionResponseDto> createCheckoutSession(
            @Valid @RequestBody CheckoutSessionRequestDto request) {

        CheckoutSessionResponseDto response =
                paymentService.createCheckoutSession(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerificationResponseDto> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequestDto request) {

        PaymentVerificationResponseDto response =
                paymentService.verifyPayment(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDetailsResponseDto> getPaymentDetails(
            @PathVariable @Positive Long paymentId) {

        PaymentDetailsResponseDto response =
                paymentService.getPaymentDetails(paymentId);

        return ResponseEntity.ok(response);
    }
}
