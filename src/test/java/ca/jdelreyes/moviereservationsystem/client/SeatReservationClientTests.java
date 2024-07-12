package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SeatReservationClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;
    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setUp() {
        final String REGEX = "(token=[^;]+)";

        restTemplate.exchange(
                "/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(authRequest()),
                Void.class
        );

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
    public void GetTheaterSeatsShouldReturnSeatResponseListAnd200HttpStatusCode() {
        ResponseEntity<List<SeatResponse>> response = restTemplate.exchange(
                "/api/seat-reservations/theater/{theaterId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<SeatResponse>>() {
                },
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(6);
    }

    @Test
    public void GetTheaterSeatsWithoutUserTokenShouldReturn403HttpStatusCode() {
        ResponseEntity<List<SeatResponse>> response = restTemplate.exchange(
                "/api/seat-reservations/theater/{theaterId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SeatResponse>>() {
                },
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void GetOwnTicketShouldReturnTicketResponseAnd200HttpStatusCode() {
        restTemplate.exchange(
                "/api/seat-reservations",
                HttpMethod.POST,
                new HttpEntity<>(createTicketRequest(), headers),
                TicketResponse.class
        );

        ResponseEntity<TicketResponse> response = restTemplate.exchange(
                "/api/seat-reservations/{ticketId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                TicketResponse.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void GetOwnTicketWithoutUserTokenShouldReturn403HttpStatusCode() {
        restTemplate.exchange(
                "/api/seat-reservations",
                HttpMethod.POST,
                new HttpEntity<>(createTicketRequest(), headers),
                TicketResponse.class
        );

        ResponseEntity<TicketResponse> response = restTemplate.exchange(
                "/api/seat-reservations/{ticketId}",
                HttpMethod.GET,
                null,
                TicketResponse.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void BuyTicketShouldReturnTicketResponseAnd201HttpStatusCode() {
        ResponseEntity<TicketResponse> response = restTemplate.exchange(
                "/api/seat-reservations",
                HttpMethod.POST,
                new HttpEntity<>(createTicketRequest(), headers),
                TicketResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString())
                .isEqualTo("http://localhost:" +
                        servletWebServerApplicationContext.getWebServer().getPort() +
                        "/api/seat-reservations/" + response.getBody().id());
    }

    @Test
    public void BuyTicketWithoutUserTokenShouldReturn403HttpStatusCode() {
        ResponseEntity<TicketResponse> response = restTemplate.exchange(
                "/api/seat-reservations",
                HttpMethod.POST,
                new HttpEntity<>(createTicketRequest()),
                TicketResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void RefundTicketShouldReturn204HttpStatusCode() {
        // buy ticket
        restTemplate.exchange(
                "/api/seat-reservations",
                HttpMethod.POST,
                new HttpEntity<>(createTicketRequest(), headers),
                TicketResponse.class
        );

        // refund
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/seat-reservations/{ticketId}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void RefundTicketWithNotOwnedOrInvalidTicketIdShouldReturn404HttpStatusCode() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/seat-reservations/{ticketId}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                1
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private AuthRequest authRequest() {
        return new AuthRequest("username", "password");
    }

    private CreateTicketRequest createTicketRequest() {
        return new CreateTicketRequest(1L, 1L);
    }
}
