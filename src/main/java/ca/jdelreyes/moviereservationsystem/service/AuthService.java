package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest) throws NotFoundException;

    AuthResponse register(AuthRequest authRequest);
}
