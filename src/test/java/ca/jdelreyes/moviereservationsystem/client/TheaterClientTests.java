package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
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

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.token());
    }

    @Test
    public void AddTheaterSeatsShouldReturnSeatResponseListAnd201HttpStatusCode() {
        restTemplate.exchange(
                "/api/theaters",
                HttpMethod.POST,
                new HttpEntity<>(createTheaterRequest(), headers),
                TheaterDetailsResponse.class
        );

        ResponseEntity<List<SeatResponse>> response = restTemplate.exchange(
                "/api/theaters/add-seats/{theaterId}",
                HttpMethod.POST,
                new HttpEntity<>(createSeatRequestList(), headers),
                new ParameterizedTypeReference<List<SeatResponse>>() {
                },
                2
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(createSeatRequestList().size());
    }

    @Test
    public void EditTheaterSeatsShouldReturnSeatResponseListAnd200HttpStatusCode() {
        restTemplate.exchange(
                "/api/theaters",
                HttpMethod.POST,
                new HttpEntity<>(createTheaterRequest(), headers),
                TheaterDetailsResponse.class
        );

        ResponseEntity<List<SeatResponse>> response = restTemplate.exchange(
                "/api/theaters/edit-seats/{theaterId}",
                HttpMethod.PUT,
                new HttpEntity<>(updateSeatRequestList(), headers),
                new ParameterizedTypeReference<List<SeatResponse>>() {
                },
                2
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(updateSeatRequestList().size());
    }


    @Test
    public void GetTheatersShouldReturnTheaterResponseListAnd200HttpStatusCode() {
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
    public void GetTheaterShouldReturnTheaterDetailsResponseAnd200HttpStatusCode() {
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
    public void GetTheaterWithNonExistingIdShouldReturn404HttpStatusCode() {
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
    public void CreateTheaterShouldReturnTheaterDetailsResponseAndHttpHeaderLocationAnd201HttpStatusCode() {
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
    public void CreateTheaterWithoutAdminTokenShouldReturn403HttpStatusCode() {
        ResponseEntity<TheaterDetailsResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                TheaterDetailsResponse.class
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void UpdateTheaterShouldReturnTheaterResponseAnd200HttpStatusCode() {
        ResponseEntity<TheaterResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateTheaterRequest(), headers),
                TheaterResponse.class,
                1
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(theaterDetailsResponseResponseEntity.getBody()).isNotNull();
        assertThat(theaterDetailsResponseResponseEntity.getBody().name()).isEqualTo(updateTheaterRequest().name());
        assertThat(theaterDetailsResponseResponseEntity.getBody().location()).isEqualTo(updateTheaterRequest().location());
    }

    @Test
    public void UpdateTheaterWithInvalidRequestBodyShouldReturn200HttpStatusCode() {
        ResponseEntity<TheaterResponse> theaterDetailsResponseResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(invalidUpdateTheaterRequest(), headers),
                TheaterResponse.class,
                1
        );

        assertThat(theaterDetailsResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void DeleteTheaterShouldReturn204HttpStatusCode() {
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
    public void DeleteTheaterWithNonExistingIdShouldReturn404HttpStatusCode() {
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/theaters/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                99999
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private List<CreateSeatRequest> createSeatRequestList() {
        return List.of(
                new CreateSeatRequest('A', 1),
                new CreateSeatRequest('A', 2),
                new CreateSeatRequest('A', 3),
                new CreateSeatRequest('B', 1),
                new CreateSeatRequest('B', 2),
                new CreateSeatRequest('C', 1),
                new CreateSeatRequest('C', 2),
                new CreateSeatRequest('C', 3)
        );
    }

    private List<UpdateSeatRequest> updateSeatRequestList() {
        return List.of(
                new UpdateSeatRequest('Z', 1),
                new UpdateSeatRequest('Z', 2),
                new UpdateSeatRequest('Z', 3),
                new UpdateSeatRequest('Y', 1),
                new UpdateSeatRequest('Y', 2),
                new UpdateSeatRequest('X', 1),
                new UpdateSeatRequest('X', 2),
                new UpdateSeatRequest('X', 3)
        );
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
