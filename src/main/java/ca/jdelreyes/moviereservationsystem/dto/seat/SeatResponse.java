package ca.jdelreyes.moviereservationsystem.dto.seat;

public record SeatResponse(Long id, Character rowLetter, Integer seatNumber,
                           Boolean isReserved) {
}
