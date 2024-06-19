package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MovieClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;

    @Test
    public void get_movies() {
        ResponseEntity<List<MovieResponse>> listResponseEntity = restTemplate.exchange(
                "/api/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieResponse>>() {
                }
        );

        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponseEntity.getBody().size()).isEqualTo(0);
    }

    // todo if there's movies in the database
    @Test
    public void get_movie() {

    }

    @Test
    public void create_movie() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies",
                HttpMethod.POST,
                new HttpEntity<>(createMovieRequest(), headers),
                MovieResponse.class
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(movieResponseResponseEntity.getHeaders().getLocation()).toString())
                .isEqualTo("http://localhost:" + servletWebServerApplicationContext.getWebServer().getPort() + "/api/movies/1");
    }

    @Test
    public void create_movie_with_invalid_request_body_should_give_400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies",
                HttpMethod.POST,
                new HttpEntity<>(createInvalidMovieRequest(), headers),
                MovieResponse.class
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test// todo create movie first
    public void update_movie() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateMovieRequest(), headers),
                MovieResponse.class
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK); //todo
    }

    private UpdateMovieRequest updateMovieRequest() {
        return new UpdateMovieRequest("updatedTitanice",
                "Updated boat sank",
                "updated avatar director",
                Genre.ACTION);
    }

    private CreateMovieRequest createMovieRequest() {
        return new CreateMovieRequest("titanic", "boat sank", "avatar director", Genre.DRAMA);
    }

    private CreateMovieRequest createInvalidMovieRequest() {
        return new CreateMovieRequest("", null, "", null);
    }

    private String getAdminToken() {
        return restTemplate.postForObject("/api/auth/authenticate",
                new AuthRequest("admin", "password"),
                AuthResponse.class).token();
    }
}
