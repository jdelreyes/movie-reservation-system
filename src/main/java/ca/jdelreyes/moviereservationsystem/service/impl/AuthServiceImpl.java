package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.exception.ConflictException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import ca.jdelreyes.moviereservationsystem.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest, HttpServletResponse response) throws NotFoundException, ConflictException {
        User user = userRepository
                .findByUsername(authRequest.username())
                .orElseThrow(NotFoundException::new);

        if (!passwordMatches(authRequest.password(), user.getPassword()))
            throw new ConflictException();

        String token = jwtService.generateToken(user);

        ResponseCookie cookieToken = ResponseCookie.from("token", token)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookieToken.toString());

        return new AuthResponse(user.getUsername(), user.getRoles());
    }

    @Override
    @Transactional
    public void register(AuthRequest authRequest) {
        User user = User.builder()
                .username(authRequest.username())
                .password(passwordEncoder.encode(authRequest.password()))
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);
    }

    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private boolean passwordMatches(String rawPassword, String hash) {
        return passwordEncoder.matches(rawPassword, hash);
    }
}
