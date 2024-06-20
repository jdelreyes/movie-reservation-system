package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record CreateMovieScheduleRequest(@Future LocalDateTime startTime, @Future LocalDateTime endTime,
                                         @NotEmpty Long movieId, @NotEmpty Long theaterId) {
}
