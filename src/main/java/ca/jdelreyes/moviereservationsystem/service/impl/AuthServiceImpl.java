package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import ca.jdelreyes.moviereservationsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public String authenticate(AuthRequest authRequest) throws NotFoundException {
        User user = userRepository
                .findByUsername(authRequest.username())
                .orElseThrow(NotFoundException::new);

        if (!passwordMatches(authRequest.password(), user.getPassword()))
            throw new NotFoundException();

        return jwtService.generateToken(new HashMap<>() {{
            put("roles", user.getRoles());
        }}, user);
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

    private boolean passwordMatches(String rawPassword, String hash) {
        return passwordEncoder.matches(rawPassword, hash);
    }
}
