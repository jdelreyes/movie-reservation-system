package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.exception.ConflictException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest, HttpServletResponse httpServletResponse) throws NotFoundException, ConflictException;

    void register(AuthRequest authRequest);

    void logout();
}
