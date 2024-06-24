package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMovieScheduleRequest(@NotNull @Future LocalDateTime startTime, @NotNull @Future LocalDateTime endTime,
                                         @NotNull Long movieId, @NotNull Long theaterId) {
}
