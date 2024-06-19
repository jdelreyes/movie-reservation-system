package ca.jdelreyes.moviereservationsystem.dto.theater;

import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;

import java.util.List;

public record TheaterDetailsResponse(Long id,
                                     String name,
                                     String location,
                                     Integer capacity,
                                     List<SeatResponse> seatResponseList) {
}
