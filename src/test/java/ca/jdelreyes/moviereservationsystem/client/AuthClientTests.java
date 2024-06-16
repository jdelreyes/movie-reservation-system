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
@Transactional
public class AuthClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    public void register() {
        AuthRequest authRequest = new AuthRequest("username", "password");

        ResponseEntity<AuthResponse> authResponseResponseEntity =
                restTemplate
                        .postForEntity("/api/auth/register",
                                authRequest,
                                AuthResponse.class);

        assertThat(authResponseResponseEntity.getBody()).isNotNull();
        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(jwtService.extractUsername(authResponseResponseEntity.getBody().token())).isEqualTo(authRequest.username());
    }

    @Test
    public void register_twice_with_same_username_should_give_403() {
        AuthRequest authRequest = new AuthRequest("username", "password");

        restTemplate
                .postForEntity("/api/auth/register",
                        authRequest,
                        AuthResponse.class);

        ResponseEntity<AuthResponse> authResponseResponseEntity =
                restTemplate
                        .postForEntity("/api/auth/register",
                                authRequest,
                                AuthResponse.class);

        assertThat(authResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
