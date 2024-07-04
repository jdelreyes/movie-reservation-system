package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;

import java.time.LocalDateTime;

public record MovieScheduleResponse(Long id,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime,
                                    MovieResponse movie,
                                    TheaterResponse theater,
                                    MovieType movieType,
                                    Boolean isCancelled) {
}
