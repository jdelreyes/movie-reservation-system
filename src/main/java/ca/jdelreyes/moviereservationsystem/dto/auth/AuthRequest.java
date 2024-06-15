package ca.jdelreyes.moviereservationsystem.dto.auth;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(@NotEmpty String username, @NotEmpty String password) {
}
