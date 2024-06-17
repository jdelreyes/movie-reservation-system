package ca.jdelreyes.moviereservationsystem.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateOwnPasswordRequest(@NotEmpty @Size(min = 8, max = 256) String oldPassword,
                                       @NotEmpty @Size(min = 8, max = 256) String newPassword) {
}
