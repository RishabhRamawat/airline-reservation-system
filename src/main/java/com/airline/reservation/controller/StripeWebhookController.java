package com.airline.reservation.controller;

import com.airline.reservation.dto.stripe.CheckoutRequest;
import com.airline.reservation.dto.stripe.CheckoutResponse;
import com.airline.reservation.dto.payment.StripeWebhookRequest;
import com.airline.reservation.dto.payment.StripeWebhookResponse;
import com.airline.reservation.service.StripeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Stripe payment operations.
 * Delegates all payment processing to the service layer.
 */
@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    private final StripeService stripeService;

    /**
     * Creates a controller with the required Stripe service.
     */
    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    /**
     * Creates a Stripe Checkout Session for a booking.
     */
    @PostMapping(
            value = "/create-checkout-session",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckoutResponse> createCheckoutSession(
            @Valid @RequestBody CheckoutRequest request) {

        log.info("POST /api/stripe/create-checkout-session: bookingId={}", request.getBookingId());

        CheckoutResponse response = stripeService.createCheckoutSession(request);

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(
            value = "/webhook",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StripeWebhookResponse> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {

        log.info("POST /api/stripe/webhook received");

        /**
         * Receives and processes Stripe webhook events.
         */

        StripeWebhookRequest request = new StripeWebhookRequest(payload, signature);
        StripeWebhookResponse response = stripeService.handleWebhook(request);

        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
}