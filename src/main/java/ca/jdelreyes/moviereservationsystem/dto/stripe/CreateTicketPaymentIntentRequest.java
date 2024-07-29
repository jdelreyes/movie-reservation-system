package ca.jdelreyes.moviereservationsystem.dto.stripe;

import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import jakarta.validation.constraints.NotNull;

public record CreateTicketPaymentIntentRequest(@NotNull MovieType movieType) {
}
