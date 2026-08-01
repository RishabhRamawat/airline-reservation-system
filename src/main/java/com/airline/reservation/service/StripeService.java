package com.airline.reservation.service;

import com.airline.reservation.dto.stripe.CheckoutRequest;
import com.airline.reservation.dto.stripe.CheckoutResponse;
import com.airline.reservation.dto.payment.StripeWebhookRequest;
import com.airline.reservation.dto.payment.StripeWebhookResponse;

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
    CheckoutResponse createCheckoutSession(CheckoutRequest request);

    /**
     * Verifies and processes a Stripe webhook event.
     *
     * @param request webhook payload and signature
     * @return webhook processing result
     */
    StripeWebhookResponse handleWebhook(StripeWebhookRequest request);
}
