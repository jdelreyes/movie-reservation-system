package ca.jdelreyes.moviereservationsystem.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record AuthRequest(@NotEmpty String username, @NotEmpty @Size(min = 8, max = 256) String password) {
}
