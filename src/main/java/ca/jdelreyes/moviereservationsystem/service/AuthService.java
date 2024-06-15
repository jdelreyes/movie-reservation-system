package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);

    AuthResponse register(AuthRequest authRequest);
}
