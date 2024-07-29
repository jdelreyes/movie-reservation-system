package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.stripe.ConfigResponse;
import ca.jdelreyes.moviereservationsystem.dto.stripe.CreateTicketPaymentIntentRequest;
import ca.jdelreyes.moviereservationsystem.dto.stripe.PaymentIntentResponse;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.service.impl.StripeServiceImpl;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {
    private final StripeServiceImpl stripeService;

    @GetMapping("/config")
    public ResponseEntity<ConfigResponse> getPublishableKey() {
        return ResponseEntity.ok(stripeService.getPublishableKey());
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponse> createTicketPaymentIntent(
//            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTicketPaymentIntentRequest createTicketPaymentIntentRequest
    ) throws StripeException {
        User user = User.builder().build();
        return ResponseEntity.ok(stripeService.createTicketPaymentIntent(user, createTicketPaymentIntentRequest));
    }
}
