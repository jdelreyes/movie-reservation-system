package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;

import java.time.LocalDateTime;

public record MovieScheduleResponse(Long id,
                                    LocalDateTime startDateTime,
                                    LocalDateTime endDateTime,
                                    LocalDateTime ticketPurchaseOpeningDateTime,
                                    LocalDateTime ticketPurchaseClosingDateTime,
                                    MovieResponse movie,
                                    TheaterResponse theater,
                                    MovieType movieType,
                                    Boolean isCancelled) {
}
