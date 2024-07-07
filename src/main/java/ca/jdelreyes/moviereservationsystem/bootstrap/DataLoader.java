package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
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
    private final MovieImageRepository movieImageRepository;

    @Override
    public void run(String... args) throws Exception {
        Movie movie = null;
        Movie movie2 = null;
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
                            .director("James Bond")
                            .genre(Genre.SCIENCE_FICTION)
                            .build()
            );

            movie2 = movieRepository.save(
                    Movie.builder()
                            .title("Titanic II")
                            .description("Titanic did not sink due to global warming")
                            .director("Mark James")
                            .genre(Genre.DRAMA)
                            .build()
            );

            FileSystemResource placeholderImage = createPlaceholderImage();

            movieImageRepository.save(
                    MovieImage.builder()
                            .data(placeholderImage.getContentAsByteArray())
                            .name(placeholderImage.getFilename())
                            .type("image/jpeg")
                            .movie(movie)
                            .build()
            );
            movieImageRepository.save(
                    MovieImage.builder()
                            .data(placeholderImage.getContentAsByteArray())
                            .name(placeholderImage.getFilename())
                            .type("image/jpeg")
                            .movie(movie2)
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
        if (!movieScheduleRepository.existsById(1L) && !movieScheduleRepository.existsById(2L)) {
            movieScheduleRepository.save(
                    MovieSchedule.builder()
                            .movie(movie)
                            .theater(theater)
                            .startDateTime(LocalDateTime.now().plusDays(2))
                            .endDateTime(LocalDateTime.now().plusDays(2).plusHours(2))
                            .ticketPurchaseOpeningDateTime(LocalDateTime.now().plusDays(1).plusHours(20))
                            .ticketPurchaseClosingDateTime(LocalDateTime.now().plusDays(1).plusHours(23))
                            .movieType(MovieType.REGULAR)
                            .isCancelled(false)
                            .build()
            );

            movieScheduleRepository.save(
                    MovieSchedule.builder()
                            .movie(movie2)
                            .theater(theater)
                            .startDateTime(LocalDateTime.now().plusDays(3))
                            .endDateTime(LocalDateTime.now().plusDays(3).plusHours(2))
                            .ticketPurchaseOpeningDateTime(LocalDateTime.now().plusDays(2).plusHours(20))
                            .ticketPurchaseClosingDateTime(LocalDateTime.now().plusDays(2).plusHours(23))
                            .movieType(MovieType.MAX)
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

    private FileSystemResource createPlaceholderImage() {
        String pathName = "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                "images" + File.separator +
                "placeholder.jpg";

        File file = new File(pathName);
        return new FileSystemResource(file);
    }
}
