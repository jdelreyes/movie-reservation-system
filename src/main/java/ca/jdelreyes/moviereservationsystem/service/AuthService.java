package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;

public interface AuthService {
    String authenticate(AuthRequest authRequest) throws NotFoundException;

    void register(AuthRequest authRequest);

    void logout();
}
