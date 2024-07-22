package ca.jdelreyes.moviereservationsystem.dto.auth;

import ca.jdelreyes.moviereservationsystem.model.enums.Role;

import java.util.Set;

public record AuthResponse(String username, Set<Role> roles) {
}
