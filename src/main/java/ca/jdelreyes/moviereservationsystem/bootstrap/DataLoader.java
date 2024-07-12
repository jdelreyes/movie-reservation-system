package ca.jdelreyes.moviereservationsystem.bootstrap;

import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import ca.jdelreyes.moviereservationsystem.model.enums.Role;
import ca.jdelreyes.moviereservationsystem.repository.*;
import ca.jdelreyes.moviereservationsystem.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${resource.url}")
    private String resourceUri;

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
                                    .description("A thief who steals corporate secrets through the use of" +
                                            " dream-sharing technology is given the inverse task of planting" +
                                            " an idea into the mind of a C.E.O.")
                                    .directors(List.of("Christopher Nolan"))
                                    .genres(Set.of(Genre.SCIENCE_FICTION, Genre.THRILLER))
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("The Lion King")
                                    .description("Lion prince Simba and his father are targeted by his bitter" +
                                            " uncle, who wants to ascend the throne himself.")
                                    .directors(List.of("Roger Allers", "Rob Minkoff"))
                                    .genres(Set.of(Genre.ANIMATION, Genre.DRAMA))
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("The Godfather")
                                    .description("The aging patriarch of an organized crime dynasty transfers" +
                                            " control of his clandestine empire to his reluctant son.")
                                    .directors(List.of("Francis Ford Coppola"))
                                    .genres(Set.of(Genre.CRIME, Genre.DRAMA))
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("Pulp Fiction")
                                    .description("The lives of two mob hitmen, a boxer, a gangster and his wife," +
                                            " and a pair of diner bandits intertwine in four tales of violence" +
                                            " and redemption.")
                                    .directors(List.of("Quentin Tarantino"))
                                    .genres(Set.of(Genre.THRILLER, Genre.CRIME))
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("Schindler's List")
                                    .description("In German-occupied Poland during World War II, Oskar Schindler" +
                                            " gradually becomes concerned for his Jewish workforce after witnessing" +
                                            " their persecution by the Nazis.")
                                    .directors(List.of("Steven Spielberg"))
                                    .genres(Set.of(Genre.HISTORICAL, Genre.DRAMA, Genre.WAR))
                                    .build()
                    ),
                    movieRepository.save(
                            Movie.builder()
                                    .title("The Notebook")
                                    .description("A poor yet passionate young man falls in love with a rich" +
                                            " young woman, giving her a sense of freedom, but they are soon" +
                                            " separated because of their social differences.")
                                    .directors(List.of("Nick Cassavetes"))
                                    .genres(Set.of(Genre.ROMANCE, Genre.DRAMA))
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
        AtomicLong counter = new AtomicLong(1);

        Arrays.stream(movies).forEach(m ->
                {
                    FileSystemResource movieImage = createMovieImage(m.getId());
                    try {
                        movieImageRepository.save(
                                movieImageRepository.save(
                                        MovieImage.builder()
                                                .data(ImageUtil.compressImage(movieImage.getContentAsByteArray()))
                                                .name(movieImage.getFilename())
                                                .type("image/png")
                                                .movie(m)
                                                .build()
                                ));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    movieScheduleRepository.save(
                            MovieSchedule.builder()
                                    .movie(m)
                                    .theater(theater)
                                    .startDateTime(LocalDateTime.now().plusDays(counter.get()))
                                    .endDateTime(LocalDateTime.now().plusDays(counter.get()).plusHours(2))
                                    .ticketPurchaseOpeningDateTime(LocalDateTime.now())
                                    .ticketPurchaseClosingDateTime(LocalDateTime.now().plusHours(2))
                                    .movieType(MovieType.REGULAR)
                                    .isCancelled(false)
                                    .build()
                    );
                    counter.getAndIncrement();
                }
        );
        movieScheduleRepository.save(
                MovieSchedule.builder()
                        .movie(movies[0])
                        .theater(theater)
                        .startDateTime(LocalDateTime.now().plusHours(2))
                        .endDateTime(LocalDateTime.now().plusHours(4))
                        .ticketPurchaseOpeningDateTime(LocalDateTime.now())
                        .ticketPurchaseClosingDateTime(LocalDateTime.now().plusHours(1).plusMinutes(59))
                        .movieType(MovieType.MAX)
                        .isCancelled(false)
                        .build()
        );

        movieScheduleRepository.save(
                MovieSchedule.builder()
                        .movie(movies[1])
                        .theater(theater)
                        .startDateTime(LocalDateTime.now().plusHours(4))
                        .endDateTime(LocalDateTime.now().plusHours(6))
                        .ticketPurchaseOpeningDateTime(LocalDateTime.now())
                        .ticketPurchaseClosingDateTime(LocalDateTime.now().plusHours(3).plusMinutes(59))
                        .movieType(MovieType.MAX)
                        .isCancelled(false)
                        .build()
        );
    }

    private FileSystemResource createMovieImage(Long id) {
        String pathName = resourceUri + id + ".png";

        File file = new File(pathName);
        return new FileSystemResource(file);
    }
}
