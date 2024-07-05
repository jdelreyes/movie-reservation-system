package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
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
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MovieScheduleClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;
    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setup() {
        AuthResponse authResponse = this.restTemplate.postForObject(
                "/api/auth/authenticate",
                new AuthRequest("admin", "password"),
                AuthResponse.class
        );

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.token());
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

        System.out.println(response.getBody());
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
}
