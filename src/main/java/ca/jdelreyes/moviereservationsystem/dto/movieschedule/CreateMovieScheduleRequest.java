package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateMovieScheduleRequest(@NotNull @Future LocalDateTime startDateTime,
                                         @NotNull @Future LocalDateTime endDateTime,
                                         @NotNull @Future LocalDateTime ticketPurchaseOpeningDateTime,
                                         @NotNull @Future LocalDateTime ticketPurchaseClosingDateTime,
                                         @NotNull Long movieId,
                                         @NotNull Long theaterId,
                                         @NotNull MovieType movieType) {
}
