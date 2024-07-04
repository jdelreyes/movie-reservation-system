package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;

import java.time.LocalDateTime;

// todo: movie and theater should be movieResponse and theaterResponse respectively
public record MovieScheduleResponse(Long id,
                                    LocalDateTime startTime,
                                    LocalDateTime endTime,
                                    Movie movie,
                                    Theater theater,
                                    MovieType movieType,
                                    Boolean isCancelled) {
}
