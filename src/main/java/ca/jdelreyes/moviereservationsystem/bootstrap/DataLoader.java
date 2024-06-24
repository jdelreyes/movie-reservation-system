package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    private final MovieScheduleRepository movieScheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        Movie movie = null;
        Theater theater = null;

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
            movie = movieRepository.save(
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
            theater = Theater.builder()
                    .name("Theater")
                    .location("123 Street")
                    .build();

            theater = theaterRepository.save(theater);

            List<Seat> seatList = createSeats(theater);

            seatRepository.saveAll(seatList);
        }

        // movie-schedule
        if (!movieScheduleRepository.existsById(1L)) {
            movieScheduleRepository.save(
                    MovieSchedule.builder()
                            .movie(movie)
                            .theater(theater)
                            .startTime(LocalDateTime.now().plusDays(2))
                            .endTime(LocalDateTime.now().plusDays(2).plusHours(2))
                            .isCancelled(false)
                            .build()
            );
        }
    }

    private List<Seat> createSeats(Theater theater) {
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
