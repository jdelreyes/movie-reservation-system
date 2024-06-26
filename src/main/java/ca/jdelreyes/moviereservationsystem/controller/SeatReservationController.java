package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.service.impl.SeatReservationServiceImpl;
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
    ) throws NotFoundException {
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
