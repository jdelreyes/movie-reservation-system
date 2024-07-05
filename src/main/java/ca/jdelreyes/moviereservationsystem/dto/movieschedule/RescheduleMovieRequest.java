package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RescheduleMovieRequest(@NotNull @Future LocalDateTime startDateTime,
                                     @NotNull @Future LocalDateTime endDateTime,
                                     @NotNull @Future LocalDateTime ticketPurchaseOpeningDateTime,
                                     @NotNull @Future LocalDateTime ticketPurchaseClosingDateTime) {

}
