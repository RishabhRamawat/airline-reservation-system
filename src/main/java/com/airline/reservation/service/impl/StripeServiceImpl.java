package com.airline.reservation.service.impl;

import com.airline.reservation.dto.stripe.CreateCheckoutSessionRequestDto;
import com.airline.reservation.dto.stripe.CreateCheckoutSessionResponseDto;
import com.airline.reservation.dto.stripe.StripeWebhookRequestDto;
import com.airline.reservation.dto.stripe.StripeWebhookResponseDto;
import com.airline.reservation.service.StripeService;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for Stripe payment operations.
 * Creates Checkout Sessions and processes Stripe webhook events.
 */
@Service
public class StripeServiceImpl implements StripeService {

    private static final Logger log = LoggerFactory.getLogger(StripeServiceImpl.class);

    // ---- Stripe event type constants ----------------------------------------
    private static final String EVENT_CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";
    private static final String EVENT_PAYMENT_INTENT_FAILED = "payment_intent.payment_failed";

    // ---- Payment status constants -------------------------------------------
    private static final String STATUS_SUCCEEDED = "SUCCEEDED";
    private static final String STATUS_FAILED    = "FAILED";
    private static final String STATUS_UNKNOWN   = "UNKNOWN";

    // ---- Injected configuration ---------------------------------------------
    private final StripeClient stripeClient;
    private final String webhookSecret;
    private final String productName;

    /**
     * Initializes the Stripe client using application configuration.
     */
    public StripeServiceImpl(
            @Value("${stripe.api-key}") String apiKey,
            @Value("${stripe.webhook-secret}") String webhookSecret,
            @Value("${stripe.product-name}") String productName) {
        this.stripeClient    = new StripeClient(apiKey);
        this.webhookSecret   = webhookSecret;
        this.productName     = productName;
    }

    /**
     * Creates a Stripe Checkout Session for a booking.
     */
    @Override
    public CreateCheckoutSessionResponseDto createCheckoutSession(
            CreateCheckoutSessionRequestDto request) {

        log.info("Creating Stripe Checkout Session for bookingId={}", request.getBookingId());

        try {
            SessionCreateParams params = buildSessionCreateParams(request);
            Session session = stripeClient.checkout().sessions().create(params);

            log.info("Checkout Session created: sessionId={}, bookingId={}",
                    session.getId(), request.getBookingId());

            return CreateCheckoutSessionResponseDto.success(session.getId(), session.getUrl());

        } catch (StripeException ex) {
            log.error("Failed to create Checkout Session for bookingId={}: code={}, message={}",
                    request.getBookingId(), ex.getCode(), ex.getMessage());
            return CreateCheckoutSessionResponseDto.failure(
                    "Payment session could not be created: " + ex.getMessage());
        }
    }

    /**
     * Verifies and processes incoming Stripe webhook events.
     */
    @Override
    public StripeWebhookResponseDto handleWebhook(StripeWebhookRequestDto request) {

        log.info("Received Stripe webhook event; verifying signature");

        Event event;
        try {
            event = Webhook.constructEvent(
                    request.getPayload(),
                    request.getSignature(),
                    webhookSecret);
        } catch (SignatureVerificationException ex) {
            log.warn("Stripe webhook signature verification failed: {}", ex.getMessage());
            return StripeWebhookResponseDto.failure(
                    "Webhook signature verification failed: " + ex.getMessage());
        }

        log.info("Webhook event verified: eventId={}, type={}", event.getId(), event.getType());

        return switch (event.getType()) {
            case EVENT_CHECKOUT_SESSION_COMPLETED -> handleCheckoutSessionCompleted(event);
            case EVENT_PAYMENT_INTENT_FAILED      -> handlePaymentIntentFailed(event);
            default                               -> buildUnknownEventResponse(event);
        };
    }

    /**
     * Builds the Checkout Session request for Stripe.
     */
    private SessionCreateParams buildSessionCreateParams(
            CreateCheckoutSessionRequestDto request) {

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productName)
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(request.getCurrency())
                        .setUnitAmount(request.getAmount())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(1L)
                        .build();

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(request.getCustomerEmail())
                .setClientReferenceId(request.getBookingId())
                .setSuccessUrl(request.getSuccessUrl())
                .setCancelUrl(request.getCancelUrl())
                .addLineItem(lineItem)
                .build();
    }

    /**
     * Handles successful Checkout Session events.
     */
    private StripeWebhookResponseDto handleCheckoutSessionCompleted(Event event) {
        log.info("Handling checkout.session.completed: eventId={}", event.getId());

        Optional<StripeObject> stripeObjectOpt = deserializeEventObject(event);

        if (stripeObjectOpt.isEmpty() || !(stripeObjectOpt.get() instanceof Session session)) {
            log.error("Could not deserialize Session from event: eventId={}", event.getId());
            return StripeWebhookResponseDto.failure(
                    "Could not deserialize checkout session from webhook event");
        }

        String paymentIntentId  = session.getPaymentIntent();
        String checkoutSessionId = session.getId();

        log.info("Payment succeeded: checkoutSessionId={}, paymentIntentId={}",
                checkoutSessionId, paymentIntentId);

        return new StripeWebhookResponseDto(
                event.getId(),
                event.getType(),
                paymentIntentId,
                checkoutSessionId,
                STATUS_SUCCEEDED,
                true,
                "Payment completed successfully");
    }

    /**
     * Handles failed payment events.
     */
    private StripeWebhookResponseDto handlePaymentIntentFailed(Event event) {
        log.info("Handling payment_intent.payment_failed: eventId={}", event.getId());

        Optional<StripeObject> stripeObjectOpt = deserializeEventObject(event);

        if (stripeObjectOpt.isEmpty()
                || !(stripeObjectOpt.get() instanceof PaymentIntent paymentIntent)) {
            log.error("Could not deserialize PaymentIntent from event: eventId={}", event.getId());
            return StripeWebhookResponseDto.failure(
                    "Could not deserialize payment intent from webhook event");
        }

        String paymentIntentId = paymentIntent.getId();
        String failureMessage  = paymentIntent.getLastPaymentError() != null
                ? paymentIntent.getLastPaymentError().getMessage()
                : "Unknown payment failure";

        log.warn("Payment failed: paymentIntentId={}, reason={}", paymentIntentId, failureMessage);

        return new StripeWebhookResponseDto(
                event.getId(),
                event.getType(),
                paymentIntentId,
                null,
                STATUS_FAILED,
                true,
                "Payment failed: " + failureMessage);
    }

    /**
     * Returns a response for unsupported webhook events.
     */
    private StripeWebhookResponseDto buildUnknownEventResponse(Event event) {
        log.debug("Unhandled Stripe event type: eventId={}, type={}",
                event.getId(), event.getType());

        return new StripeWebhookResponseDto(
                event.getId(),
                event.getType(),
                null,
                null,
                STATUS_UNKNOWN,
                true,
                "Event type not handled: " + event.getType());
    }

    /**
     * Safely deserializes the Stripe event object.
     */
    private Optional<StripeObject> deserializeEventObject(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        Optional<StripeObject> objectOpt = deserializer.getObject();

        if (objectOpt.isPresent()) {
            return objectOpt;
        }

        log.warn("API version mismatch for event={} (eventApiVersion={}, sdkPinnedVersion={}). "
                        + "Falling back to unsafe deserialization.",
                event.getId(), event.getApiVersion(),
                com.stripe.Stripe.API_VERSION);

        try {
            StripeObject stripeObject = deserializer.deserializeUnsafe();
            return Optional.ofNullable(stripeObject);
        } catch (Exception ex) {
            log.error("Unsafe deserialization failed for eventId={}: {}",
                    event.getId(), ex.getMessage());
            return Optional.empty();
        }
    }
}
