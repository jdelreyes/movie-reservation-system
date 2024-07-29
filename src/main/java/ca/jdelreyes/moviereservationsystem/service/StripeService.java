package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.stripe.ConfigResponse;
import ca.jdelreyes.moviereservationsystem.dto.stripe.CreateTicketPaymentIntentRequest;
import ca.jdelreyes.moviereservationsystem.dto.stripe.PaymentIntentResponse;
import ca.jdelreyes.moviereservationsystem.model.User;
import com.stripe.exception.StripeException;

public interface StripeService {
    PaymentIntentResponse createTicketPaymentIntent(
            User user, CreateTicketPaymentIntentRequest createTicketPaymentIntentRequest
    ) throws StripeException;

    ConfigResponse getPublishableKey();
}
