package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.Seat;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.repository.SeatRepository;
import ca.jdelreyes.moviereservationsystem.repository.TheaterRepository;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        // user
        if (!userRepository.existsByUsername("admin")) {
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(Role.ADMIN))
                    .build();

            userRepository.save(user);
        }
        // movie
        if (!movieRepository.existsById(1L) && !movieRepository.existsById(2L)) {
            movieRepository.save(
                    Movie.builder()
                            .title("Jack The Builder")
                            .description("A builder from the future")
                            .genre(Genre.SCIENCE_FICTION)
                            .build()
            );

            movieRepository.save(
                    Movie.builder()
                            .title("Titanic II")
                            .description("Titanic did not sink due to global warming")
                            .genre(Genre.DRAMA)
                            .build()
            );
        }
        // theater
        if (!theaterRepository.existsById(1L)) {
            Theater theater = Theater.builder()
                    .name("Theater")
                    .location("123 Street")
                    .build();

            List<Seat> seatList = createSeat(theater);

            theaterRepository.save(theater);
            seatRepository.saveAll(seatList);
        }
    }

    private List<Seat> createSeat(Theater theater) {
        return List.of(Seat.builder()
                        .seatNumber(1)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build(),
                Seat.builder()
                        .seatNumber(2)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build(),
                Seat.builder()
                        .seatNumber(3)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build(),
                Seat.builder()
                        .seatNumber(4)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build(),
                Seat.builder()
                        .seatNumber(5)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build(),
                Seat.builder()
                        .seatNumber(6)
                        .rowLetter('A')
                        .isReserved(true)
                        .theater(theater)
                        .build());
    }
}
