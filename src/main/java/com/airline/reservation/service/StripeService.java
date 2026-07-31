package com.airline.reservation.service;

import com.airline.reservation.dto.stripe.CreateCheckoutSessionRequestDto;
import com.airline.reservation.dto.stripe.CreateCheckoutSessionResponseDto;
import com.airline.reservation.dto.stripe.StripeWebhookRequestDto;
import com.airline.reservation.dto.stripe.StripeWebhookResponseDto;

/**
 * Service contract for Stripe payment operations.
 * Handles Checkout Session creation and webhook processing.
 */
public interface StripeService {

    /**
     * Creates a Stripe Checkout Session.
     *
     * @param request checkout session details
     * @return checkout session response
     */
    CreateCheckoutSessionResponseDto createCheckoutSession(CreateCheckoutSessionRequestDto request);

    /**
     * Verifies and processes a Stripe webhook event.
     *
     * @param request webhook payload and signature
     * @return webhook processing result
     */
    StripeWebhookResponseDto handleWebhook(StripeWebhookRequestDto request);
}
