package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MovieScheduleClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;
    private final HttpHeaders headers = new HttpHeaders();
    private final int movieScheduleCount = 8;
    private final String movieScheduleUri = "/api/movie-schedules";

    @BeforeEach
    public void setUp() {
        final String REGEX = "(token=[^;]+)";

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/auth/authenticate",
                HttpMethod.POST,
                new HttpEntity<>(authRequest()),
                Void.class
        );

        final String SET_COOKIE = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(SET_COOKIE);

        if (matcher.find()) {
            headers.add(HttpHeaders.COOKIE, matcher.group(1));
        }
    }

    @Test
    public void GetMovieSchedulesShouldReturnMovieScheduleResponseListAnd200HttpStatusCode() {
        ResponseEntity<List<MovieScheduleResponse>> response = restTemplate.exchange(
                movieScheduleUri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieScheduleResponse>>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(movieScheduleCount);

        response = restTemplate.exchange(
                movieScheduleUri + "?theater=1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieScheduleResponse>>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(movieScheduleCount);

        response = restTemplate.exchange(
                movieScheduleUri + "?movie=1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieScheduleResponse>>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    public void AirMovieShouldReturnMovieScheduleResponseAnd201HttpStatusCode() {
        CreateMovieScheduleRequest createMovieScheduleRequest = createMovieScheduleRequest();

        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules",
                HttpMethod.POST,
                new HttpEntity<>(createMovieScheduleRequest, headers),
                MovieScheduleResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().startDateTime()).isEqualTo(createMovieScheduleRequest.startDateTime());
        assertThat(response.getBody().endDateTime()).isEqualTo(createMovieScheduleRequest.endDateTime());
    }

    @Test
    public void AirMovieWithInvalidRequestBodyShouldReturn400HttpStatusCode() {
        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules",
                HttpMethod.POST,
                new HttpEntity<>(createInvalidMovieScheduleRequest(), headers),
                MovieScheduleResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void CancelMovieShouldReturnMovieScheduleResponseAnd200HttpStatusCode() {
        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules/cancel-movie/{movieScheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                MovieScheduleResponse.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isCancelled()).isEqualTo(true);
    }

    @Test
    public void CancelMovieWithInvalidIdShouldReturn404HttpStatusCode() {
        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules/cancel-movie/{movieScheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                MovieScheduleResponse.class,
                999999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void RescheduleMovieShouldReturnMovieScheduleResponseAnd200HttpStatusCode() {
        RescheduleMovieRequest rescheduleMovieRequest = rescheduleMovieRequest();

        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules/reschedule-movie/{movieScheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(rescheduleMovieRequest, headers),
                MovieScheduleResponse.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().startDateTime()).isEqualTo(rescheduleMovieRequest.startDateTime());
        assertThat(response.getBody().endDateTime()).isEqualTo(rescheduleMovieRequest.endDateTime());
    }

    @Test
    public void RescheduleMovieWithInvalidIdShouldReturn404HttpStatusCode() {
        ResponseEntity<MovieScheduleResponse> response = restTemplate.exchange(
                "/api/movie-schedules/reschedule-movie/{movieScheduleId}",
                HttpMethod.PUT,
                new HttpEntity<>(rescheduleMovieRequest(), headers),
                MovieScheduleResponse.class,
                9999999
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private RescheduleMovieRequest rescheduleMovieRequest() {
        return new RescheduleMovieRequest(LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(2),
                LocalDateTime.now().plusDays(6).plusHours(20),
                LocalDateTime.now().plusDays(6).plusHours(23)
        );
    }

    private CreateMovieScheduleRequest createMovieScheduleRequest() {
        return new CreateMovieScheduleRequest(
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(2),
                LocalDateTime.now().plusDays(1).plusHours(20),
                LocalDateTime.now().plusDays(1).plusHours(23),
                1L,
                1L,
                MovieType.REGULAR
        );
    }

    private CreateMovieScheduleRequest createInvalidMovieScheduleRequest() {
        return new CreateMovieScheduleRequest(null, null, null, null, 0L, 999L, null);
    }

    private AuthRequest authRequest() {
        return new AuthRequest("admin", "password");
    }
}
