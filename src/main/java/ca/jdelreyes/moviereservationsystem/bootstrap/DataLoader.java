package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.*;
import ca.jdelreyes.moviereservationsystem.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

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
        Theater theater = null;

        // user
        if (!userRepository.existsByUsername("admin")) {
            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(Set.of(Role.ADMIN, Role.ACTUATOR))
                    .build();

            userRepository.save(user);
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

        // movie
        if (!movieRepository.existsById(1L)) {
            createMovieImagesAndSchedules(theater, new Movie[]{
                    movieRepository.save(
                            Movie.builder()
                                    .title("Inception")
                                    .description("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.")
                                    .director("Christopher Nolan")
                                    .genre(Genre.SCIENCE_FICTION)
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("The Lion King")
                                    .description("Lion prince Simba and his father are targeted by his bitter uncle, who wants to ascend the throne himself.")
                                    .director("Roger Allers, Rob Minkoff")
                                    .genre(Genre.ANIMATION)
                                    .build()
                    ),
                    movieRepository.save(Movie.builder()
                            .title("The Godfather")
                            .description("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.")
                            .director("Francis Ford Coppola")
                            .genre(Genre.CRIME)
                            .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("Pulp Fiction")
                                    .description("The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.")
                                    .director("Quentin Tarantino")
                                    .genre(Genre.THRILLER)
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("Schindler's List")
                                    .description("In German-occupied Poland during World War II, Oskar Schindler gradually becomes concerned for his Jewish workforce after witnessing their persecution by the Nazis.")
                                    .director("Steven Spielberg")
                                    .genre(Genre.HISTORICAL)
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("The Notebook")
                                    .description("A poor yet passionate young man falls in love with a rich young woman, giving her a sense of freedom, but they are soon separated because of their social differences.")
                                    .director("Nick Cassavetes")
                                    .genre(Genre.ROMANCE)
                                    .build()
                    )});
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

    private void createMovieImagesAndSchedules(Theater theater, Movie[] movies) {
        FileSystemResource placeholderImage = createPlaceholderImage();
        AtomicLong counter = new AtomicLong(2);

        Arrays.stream(movies).forEach(m ->
                {
                    try {
                        movieImageRepository.save(
                                movieImageRepository.save(
                                        MovieImage.builder()
                                                .data(ImageUtil.compressImage(placeholderImage.getContentAsByteArray()))
                                                .name(placeholderImage.getFilename())
                                                .type("image/jpeg")
                                                .movie(m)
                                                .build()
                                ));
                        movieScheduleRepository.save(
                                MovieSchedule.builder()
                                        .movie(m)
                                        .theater(theater)
                                        .startDateTime(LocalDateTime.now().plusDays(counter.get()))
                                        .endDateTime(LocalDateTime.now().plusDays(counter.get()).plusHours(2))
                                        .ticketPurchaseOpeningDateTime(LocalDateTime.now().plusDays(counter.get() - 1).plusHours(20))
                                        .ticketPurchaseClosingDateTime(LocalDateTime.now().plusDays(counter.get() - 1).plusHours(23))
                                        .movieType(MovieType.MAX)
                                        .isCancelled(false)
                                        .build()
                        );
                        counter.getAndIncrement();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
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
