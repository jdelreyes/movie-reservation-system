package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import ca.jdelreyes.moviereservationsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) throws NotFoundException {
        User user = userRepository
                .findUserByUsername(authRequest.username())
                .orElseThrow(NotFoundException::new);

        if (!passwordMatches(authRequest.password(), user.getPassword()))
            throw new NotFoundException();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse register(AuthRequest authRequest) {
        User user = User.builder()
                .username(authRequest.username())
                .password(passwordEncoder.encode(authRequest.password()))
                .roles(Set.of(Role.USER))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    private boolean passwordMatches(String rawPassword, String hash) {
        return passwordEncoder.matches(rawPassword, hash);
    }
}
