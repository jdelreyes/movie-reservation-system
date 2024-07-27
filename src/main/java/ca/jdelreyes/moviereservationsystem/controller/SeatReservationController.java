package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.service.impl.SeatReservationServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/seat-reservations")
@RequiredArgsConstructor
public class SeatReservationController {
    private final SeatReservationServiceImpl seatReservationService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<SeatResponse>> getTheaterSeats(
            @PathVariable("theaterId") Long theaterId
    ) throws NotFoundException {
        return ResponseEntity.ok(seatReservationService.getTheaterSeats(theaterId));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getOwnTicket(
            @AuthenticationPrincipal User user,
            @PathVariable("ticketId") Long ticketId
    ) throws NotFoundException {
        return ResponseEntity.ok(seatReservationService.getOwnTicket(user, ticketId));
    }

    @PostMapping
    public ResponseEntity<TicketResponse> buyTicket(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTicketRequest createTicketRequest
    ) throws NotFoundException, StripeException {
        // todo: this probably needs to be in a separate endpoint after user has submitted which movie they want
        // todo: understand payment intent
        PaymentIntentCreateParams createdPaymentIntent = PaymentIntentCreateParams.builder()
                .setAmount(1L)
                .setCurrency("cad")
                .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(createdPaymentIntent);
        // todo end

        TicketResponse ticketResponse = seatReservationService.buyTicket(user, createTicketRequest);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{ticketId}")
                                .buildAndExpand(ticketResponse.id())
                                .toUri()
                ).body(ticketResponse);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> refundTicket(
            @AuthenticationPrincipal User user,
            @PathVariable("ticketId") Long ticketId
    ) throws NotFoundException {
        seatReservationService.refundTicket(user, ticketId);

        return ResponseEntity.noContent().build();
    }
}
