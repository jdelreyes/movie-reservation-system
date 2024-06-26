package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;

public interface SeatReservationService {
    TicketResponse buyTicket(CreateTicketRequest createTicketRequest);

    void refundTicket(String ticketId);
}
