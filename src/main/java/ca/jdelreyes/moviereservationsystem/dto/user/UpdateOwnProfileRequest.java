package ca.jdelreyes.moviereservationsystem.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UpdateOwnProfileRequest(@NotEmpty String username) {
}
