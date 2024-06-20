package ca.jdelreyes.moviereservationsystem.dto.theater;

import jakarta.validation.constraints.NotEmpty;

public record CreateTheaterRequest(@NotEmpty String name,
                                   @NotEmpty String location) {
}
