package ca.jdelreyes.moviereservationsystem.dto.seat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateSeatRequest(@NotNull Character rowLetter,
                                @NotNull Integer seatNumber) {
}
