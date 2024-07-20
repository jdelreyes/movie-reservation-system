package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtServiceImpl jwtService;

    private final AuthRequest authRequest = new AuthRequest("username", "password");

    @Test
    public void RegisterShouldReturnAuthResponseAnd204HttpStatusCode() {
        ResponseEntity<Void> response = registerUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void AuthenticateShouldReturnAuthResponseAnd200HttpStatusCode() {
        registerUser();
        ResponseEntity<Void> response = authenticate();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).isNotNull();
    }

    @Test
    public void AuthenticateWithWrongCredentialsShouldReturn404HttpStatusCode() {
        registerUser();
        AuthRequest wrongAuthenticateCredentials = new AuthRequest("wrongUsername", "wrongPassword");

        ResponseEntity<Void> authResponseResponseEntity =
                restTemplate
                        .postForEntity("/api/auth/authenticate",
                                wrongAuthenticateCredentials,
                                Void.class);

        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void RegisterTwiceWithTheSameUsernameShouldReturn403HttpStatusCode() {
        registerUser();
        ResponseEntity<Void> response = registerUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Void> registerUser() {
        return restTemplate.postForEntity("/api/auth/register",
                authRequest,
                Void.class);
    }

    private ResponseEntity<Void> authenticate() {
        return restTemplate.postForEntity("/api/auth/authenticate",
                authRequest,
                Void.class);
    }
}
