package com.airline.reservation.dto.stripe;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a Stripe Checkout Session.
 *
 * <p>Carries all required data to construct a hosted payment page via the Stripe Checkout API.
 * The {@code amount} must be expressed in the smallest currency unit (e.g., paise for INR,
 * cents for USD).
 */
public class CheckoutRequest {

    /**
     * Internal booking identifier used as the Stripe {@code clientReferenceId}
     * so the session can be reconciled with the booking record after payment.
     */
    @NotBlank(message = "Booking ID must not be blank")
    private String bookingId;

    /**
     * Payment amount in the smallest currency unit (e.g., cents, paise).
     * Must be at least 1.
     */
    @NotNull(message = "Amount must not be null")
    @Min(value = 1, message = "Amount must be at least 1")
    private Long amount;

    /**
     * Three-letter ISO 4217 currency code in lowercase (e.g., {@code usd}, {@code inr}).
     */
    @NotBlank(message = "Currency must not be blank")
    @Pattern(regexp = "^[a-z]{3}$", message = "Currency must be a 3-letter lowercase ISO 4217 code")
    private String currency;

    /**
     * Customer's email address, pre-filled on the Stripe Checkout page.
     */
    @NotBlank(message = "Customer email must not be blank")
    @Email(message = "Customer email must be a valid email address")
    private String customerEmail;

    /**
     * URL to redirect the customer to after a successful payment.
     */
    @NotBlank(message = "Success URL must not be blank")
    @Size(max = 2048, message = "Success URL must not exceed 2048 characters")
    private String successUrl;

    /**
     * URL to redirect the customer to if they cancel the payment.
     */
    @NotBlank(message = "Cancel URL must not be blank")
    @Size(max = 2048, message = "Cancel URL must not exceed 2048 characters")
    private String cancelUrl;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public CheckoutRequest() {
    }

    public CheckoutRequest(
            String bookingId,
            Long amount,
            String currency,
            String customerEmail,
            String successUrl,
            String cancelUrl) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.currency = currency;
        this.customerEmail = customerEmail;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}