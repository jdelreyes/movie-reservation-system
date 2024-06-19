package ca.jdelreyes.moviereservationsystem.dto.theater;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateTheaterRequest(@NotEmpty String name,
                                   @NotEmpty String location,
                                   @NotNull Integer capacity) {
}
