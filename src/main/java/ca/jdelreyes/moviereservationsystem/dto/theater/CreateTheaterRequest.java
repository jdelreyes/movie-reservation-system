package ca.jdelreyes.moviereservationsystem.dto.theater;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateTheaterRequest(@NotEmpty String name,
                                   @NotEmpty String location,
                                   @NotNull Integer capacity) {
}
