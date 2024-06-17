package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class UserClientTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void get_users_should_return_UserResponse_List() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());

        ResponseEntity<List<UserResponse>> listResponseEntity = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<UserResponse>>() {
                }
        );

        assertThat(listResponseEntity).isNotNull();
        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponseEntity.getBody().size()).isEqualTo(1);
    }

    @Test
    public void get_user_by_id_should_return_UserResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());

        final int userId = 1;

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserResponse.class,
                userId
        );

        assertThat(userResponseResponseEntity).isNotNull();
        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects
                .requireNonNull(userResponseResponseEntity.getBody()).username())
                .isEqualTo("username");
    }

    @Test
    public void get_user_by_wrong_id_should_return_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());

        final int userId = 2;

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserResponse.class,
                userId
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    public String getToken() {
        return restTemplate.postForObject("/api/auth/register",
                new AuthRequest("username", "password"),
                AuthResponse.class).token();
    }
}
