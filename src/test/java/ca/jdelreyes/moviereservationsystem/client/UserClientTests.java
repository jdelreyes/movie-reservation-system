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
    public void get_users_should_return_UserResponse_List() {
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
    public void get_users_without_token_should_return_403() {
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
    public void get_user_by_id_should_return_UserResponse() {
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
    public void get_user_by_wrong_id_should_return_404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        final int userId = 3;

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserResponse.class,
                userId
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void update_own_profile_should_return_200() {
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
    public void update_own_profile_without_body_should_return_422() {
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
    public void update_own_password_should_return_204() {
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
    public void update_own_password_with_wrong_old_password_should_return_400() {
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
    public void delete_own_account_should_return_204() {
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
    public void delete_own_account_without_token_should_return_403() {
        ResponseEntity<Void> voidResponseEntity = restTemplate.exchange(
                "/api/users/delete-account",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_user_should_give_200() {
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
    public void delete_user_should_give_204() {
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
    public void update_user_without_admin_role_should_give_403() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getUserToken());

        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.exchange(
                "/api/users/2",
                HttpMethod.PUT,
                new HttpEntity<>(updateUserRequest(), headers),
                UserResponse.class
        );

        assertThat(userResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_user_without_admin_role_should_give_403() {
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
