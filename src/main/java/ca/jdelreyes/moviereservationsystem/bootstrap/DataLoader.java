package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(Role.ADMIN))
                    .build();

            userRepository.save(user);
        }
    }
}
