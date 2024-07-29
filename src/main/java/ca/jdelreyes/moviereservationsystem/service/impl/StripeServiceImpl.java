package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.stripe.ConfigResponse;
import ca.jdelreyes.moviereservationsystem.dto.stripe.CreateTicketPaymentIntentRequest;
import ca.jdelreyes.moviereservationsystem.dto.stripe.PaymentIntentResponse;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.service.StripeService;
import ca.jdelreyes.moviereservationsystem.util.Mapper;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {
    @Value("${stripe.publishable.key}")
    private String stripePublishableKey;

    @Override
    public PaymentIntentResponse createTicketPaymentIntent(User user, CreateTicketPaymentIntentRequest createTicketPaymentIntentRequest) throws StripeException {
        Double price = createTicketPaymentIntentRequest.movieType().price;

        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .addPaymentMethodType("card")
                .setAmount(price.longValue() * 100)
                .setCurrency("cad")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        return Mapper.mapPaymentIntentToPaymentIntentResponse(paymentIntent);
    }

    @Override
    public ConfigResponse getPublishableKey() {
        return new ConfigResponse(stripePublishableKey);
    }
}
