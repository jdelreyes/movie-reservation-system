package ca.jdelreyes.moviereservationsystem.dto.ticket;

import java.time.LocalDateTime;

public record TicketResponse(Long id,
                             Double price,
                             LocalDateTime purchaseTime,
                             Character rowLetter,
                             Integer seatNumber,
                             String theaterName,
                             String location,
                             LocalDateTime startTime,
                             LocalDateTime endTime) {
}
