package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.UpdateTheaterRequest;
import org.junit.jupiter.api.BeforeEach;
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
public class TheaterClientTests {
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

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+authResponse.token());
    }

    @Test
    public void get_theaters() {
        ResponseEntity<List<TheaterResponse>> theaterListEntity = restTemplate.exchange(
                "/api/theaters",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TheaterResponse>>() {
                }
        );

        assertThat(theaterListEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(theaterListEntity.getBody()).isNotNull();
        assertThat(theaterListEntity.getBody().size()).isEqualTo(1);
    }

    @Test
    public void get_theater() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.GET,
                null,
                TheaterDetailsResponse.class,
                1
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(theaterDetailsResponseResponseEntity.getBody()).isNotNull();
        assertThat(theaterDetailsResponseResponseEntity.getBody().name()).isEqualTo("Theater");
    }

    @Test
    public void get_non_existing_theater_should_return_404() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.GET,
                null,
                TheaterDetailsResponse.class,
                9999999
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void create_theater() {
        System.out.println(headers);

        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters",
                HttpMethod.POST,
                new HttpEntity<>(createTheaterRequest(), headers),
                TheaterDetailsResponse.class
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(theaterDetailsResponseResponseEntity.getHeaders()).isNotNull();
        assertThat(
                Objects.requireNonNull(theaterDetailsResponseResponseEntity
                                .getHeaders()
                                .getLocation())
                        .toString())
                .isEqualTo("http://localhost:" +
                        servletWebServerApplicationContext.getWebServer().getPort() +
                        "/api/theaters/2"
                );
        assertThat(theaterDetailsResponseResponseEntity.getBody()).isNotNull();
        assertThat(theaterDetailsResponseResponseEntity.getBody().name()).isEqualTo(createTheaterRequest().name());
        assertThat(theaterDetailsResponseResponseEntity.getBody().location()).isEqualTo(createTheaterRequest().location());
    }

    @Test
    public void create_theater_without_admin_token_should_return_403() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                TheaterDetailsResponse.class
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_theater() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateTheaterRequest(), headers),
                TheaterDetailsResponse.class,
                1
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(theaterDetailsResponseResponseEntity.getBody()).isNotNull();
        assertThat(theaterDetailsResponseResponseEntity.getBody().name()).isEqualTo(updateTheaterRequest().name());
        assertThat(theaterDetailsResponseResponseEntity.getBody().location()).isEqualTo(updateTheaterRequest().location());
    }

    @Test
    public void update_theater_with_invalid_request_body_should_return_400() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(invalidUpdateTheaterRequest(), headers),
                TheaterDetailsResponse.class,
                1
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void delete_theater() {
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                1
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void delete_non_existing_theater_should_return_404() {
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                99999
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private UpdateTheaterRequest updateTheaterRequest() {
        return new UpdateTheaterRequest("updateTheater", "updatedStreet");
    }

    private UpdateTheaterRequest invalidUpdateTheaterRequest() {
        return new UpdateTheaterRequest("", null);
    }

    private CreateTheaterRequest createTheaterRequest() {
        return new CreateTheaterRequest("Theater2", "456 Street");
    }
}
