package ca.jdelreyes.moviereservationsystem.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(@NotEmpty String password) {
}
