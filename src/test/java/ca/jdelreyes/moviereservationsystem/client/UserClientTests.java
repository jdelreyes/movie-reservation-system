package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnPasswordRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnProfileRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
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
    public void GetUsersShouldReturnUserResponseListAnd200HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<List<UserResponse>> listResponseEntity = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<UserResponse>>() {
                }
        );

        assertThat(listResponseEntity).isNotNull();
        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponseEntity.getBody().size()).isEqualTo(2);
    }

    @Test
    public void GetUsersWithoutTokenShouldReturn403HttpStatusCode() {
        ResponseEntity<List<UserResponse>> listResponseEntity = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResponse>>() {
                }
        );

        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void GetUserByIdShouldReturnUserResponseAnd200HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        final int userId = 2;

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
    public void GetUserWithNonExistingIdShouldReturn404HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserResponse.class,
                999999
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void UpdateOwnProfileShouldReturnUserResponseAnd200HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/update-profile",
                HttpMethod.PUT,
                new HttpEntity<>(updateOwnProfileRequest(), headers),
                UserResponse.class
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userResponseResponseEntity.getBody().username()).isEqualTo(updateOwnProfileRequest().username());
    }

    @Test
    public void UpdateOwnProfileWithoutRequestBodyShouldReturn422HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/update-profile",
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                UserResponse.class
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void UpdateOwnPasswordShouldReturn204HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/update-password",
                HttpMethod.PUT,
                new HttpEntity<>(updateOwnPasswordRequest(), headers),
                Void.class
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void UpdateOwnPasswordWithInvalidOrWrongOldPasswordShouldReturn400HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/update-password",
                HttpMethod.PUT,
                new HttpEntity<>(updateOwnWrongPasswordRequest(), headers),
                Void.class
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void DeleteOwnAccountShouldReturn204HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/delete-account",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void DeleteOwnAccountWithoutTokenShouldReturn403HttpStatusCode() {
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/delete-account",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void UpdateUserShouldReturnUserResponseAnd200HttpStatusCode() {
        getUserToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/2",
                HttpMethod.PUT,
                new HttpEntity<>(updateUserRequest(), headers),
                UserResponse.class
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void DeleteUserShouldReturn204HttpStatusCode() {
        getUserToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                2
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void UpdateUserWithoutAdminTokenShouldReturn403HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateUserRequest(), headers),
                UserResponse.class,
                99999
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void DeleteUserWithoutAdminTokenShouldReturn403HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/2",
                HttpMethod.DELETE,
                null,
                Void.class
        );


        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String getAdminToken() {
        return restTemplate.postForObject("/api/auth/authenticate",
                new AuthRequest("admin", "password"),
                AuthResponse.class).token();
    }

    private String getUserToken() {
        return restTemplate.postForObject("/api/auth/register",
                new AuthRequest("username", "password"),
                AuthResponse.class).token();
    }

    private UpdateOwnProfileRequest updateOwnProfileRequest() {
        return new UpdateOwnProfileRequest("updatedUsername");
    }

    private UpdateOwnPasswordRequest updateOwnPasswordRequest() {
        return new UpdateOwnPasswordRequest("password", "updatedPassword");
    }

    private UpdateOwnPasswordRequest updateOwnWrongPasswordRequest() {
        return new UpdateOwnPasswordRequest("wrongPassword", "updatedPassword");
    }

    private UpdateUserRequest updateUserRequest() {
        return new UpdateUserRequest("updateUsername");
    }
}
