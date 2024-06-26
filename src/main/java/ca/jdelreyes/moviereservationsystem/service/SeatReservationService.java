package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;

import java.util.List;

public interface SeatReservationService {
    List<SeatResponse> getTheaterSeats(Long theaterId) throws NotFoundException;

    TicketResponse getOwnTicket(User user, Long ticketId) throws NotFoundException;

    TicketResponse buyTicket(User user, CreateTicketRequest createTicketRequest) throws NotFoundException;

    void refundTicket(User user, Long ticketId) throws NotFoundException;
}
