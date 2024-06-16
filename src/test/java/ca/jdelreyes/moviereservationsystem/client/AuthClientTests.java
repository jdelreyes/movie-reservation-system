package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional()
public class AuthClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtServiceImpl jwtService;

    private final AuthRequest authRequest = new AuthRequest("username", "password");

    @Test
    public void register() {
        ResponseEntity<AuthResponse> authResponseResponseEntity = registerUser();

        assertThat(authResponseResponseEntity.getBody()).isNotNull();
        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jwtService.extractUsername(authResponseResponseEntity.getBody().token())).isEqualTo(authRequest.username());
    }

    @Test
    public void authenticate() {
        ResponseEntity<AuthResponse> authResponseResponseEntity = registerUser();

        assertThat(authResponseResponseEntity.getBody()).isNotNull();
        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jwtService.extractUsername(authResponseResponseEntity.getBody().token())).isEqualTo(authRequest.username());
    }

    @Test
    public void authenticate_with_wrong_credentials_should_return_404() {
        registerUser();
        AuthRequest wrongAuthenticateCredentials = new AuthRequest("wrongUsername", "wrongPassword");

        ResponseEntity<AuthResponse> authResponseResponseEntity =
                restTemplate
                        .postForEntity("/api/auth/authenticate",
                                wrongAuthenticateCredentials,
                                AuthResponse.class);

        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void register_twice_with_same_username_should_return_403() {
        registerUser();
        ResponseEntity<AuthResponse> authResponseResponseEntity = registerUser();

        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<AuthResponse> registerUser() {
        return restTemplate.postForEntity("/api/auth/register",
                authRequest,
                AuthResponse.class);
    }
}
