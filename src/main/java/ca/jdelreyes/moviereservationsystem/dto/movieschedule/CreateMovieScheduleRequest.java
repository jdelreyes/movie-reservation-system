package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record CreateMovieScheduleRequest(@NotNull @Future LocalDateTime startTime,
                                         @NotNull @Future LocalDateTime endTime,
                                         @NotNull Long movieId,
                                         @NotNull Long theaterId,
                                         @NotNull MovieType movieType) {
}
