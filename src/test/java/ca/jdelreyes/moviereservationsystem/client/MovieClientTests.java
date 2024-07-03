package ca.jdelreyes.moviereservationsystem.client;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.dto.auth.AuthResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MovieClientTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ServletWebServerApplicationContext servletWebServerApplicationContext;

    @Test
    public void GetMoviesShouldReturnMovieResponseListAnd200HttpStatusCode() {
        ResponseEntity<List<MovieResponse>> listResponseEntity = restTemplate.exchange(
                "/api/movies",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MovieResponse>>() {
                }
        );

        assertThat(listResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponseEntity.getBody()).isNotNull();
        assertThat(listResponseEntity.getBody().size()).isEqualTo(2);
    }

    @Test
    public void GetMovieShouldReturnMovieResponseAnd200HttpStatusCode() {
        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.GET,
                null,
                MovieResponse.class,
                1
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(movieResponseResponseEntity.getBody()).isNotNull();
        // from DataLoader class
        assertThat(movieResponseResponseEntity.getBody().title()).isEqualTo("Jack The Builder");
    }

    @Test
    public void uploadMovieImageShouldReturnHttpHeaderLocationAnd201HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        URI uri;
        String pathName = "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator +
                "images" + File.separator +
                "image.jpeg";
        try {
            uri = new URI("http://localhost:" +
                    servletWebServerApplicationContext.getWebServer().getPort() +
                    "/api/movies/1/image");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Optional<File> file = Optional.of(new File(pathName));

        file.ifPresent(f -> {
            FileSystemResource fileSystemResource = new FileSystemResource(f);

            ResponseEntity<MovieImageResponse> response = restTemplate.exchange(
                    "/api/movies/{id}/image",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new LinkedMultiValueMap<String, Object>() {{
                                add("image", fileSystemResource);
                            }},
                            headers
                    ),
                    MovieImageResponse.class,
                    1
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().name()).isEqualTo("image");
            assertThat(response.getBody().type()).isEqualTo("image/jpeg");
            assertThat(response.getHeaders()).isNotNull();
            assertThat(response.getHeaders().getLocation())
                    .isEqualTo(uri);

            response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    MovieImageResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().name()).isEqualTo("image");
            assertThat(response.getBody().type()).isEqualTo("image/jpeg");
        });
    }

    @Test
    public void CreateMovieShouldReturnHttpHeaderLocationAnd201HttpStatusCode() {
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
                .isEqualTo("http://localhost:" +
                        servletWebServerApplicationContext.getWebServer().getPort() +
                        "/api/movies/3");
    }

    @Test
    public void CreateMovieWithInvalidRequestBodyShouldReturn400HttpStatusCode() {
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

    @Test
    public void UpdateMovieShouldReturnUpdatedMovieResponseAnd200HttpStatusCode() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateMovieRequest(), headers),
                MovieResponse.class,
                1
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(movieResponseResponseEntity.getBody()).isNotNull();
        assertThat(movieResponseResponseEntity.getBody().title()).isEqualTo(updateMovieRequest().title());
        assertThat(movieResponseResponseEntity.getBody().description()).isEqualTo(updateMovieRequest().description());
    }

    @Test
    public void UpdateMovieWithInvalidRequestBodyShouldReturn400() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<MovieResponse> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateInvalidMovieRequest(), headers),
                MovieResponse.class,
                1
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void DeleteMovieShouldReturn204() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<Void> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                1
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void DeleteNonExistingMovieShouldReturn404() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken());

        ResponseEntity<Void> movieResponseResponseEntity = restTemplate.exchange(
                "/api/movies/{id}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class,
                999999
        );

        assertThat(movieResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
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

    private UpdateMovieRequest updateInvalidMovieRequest() {
        return new UpdateMovieRequest("", null, "", null);
    }

    private String getAdminToken() {
        return restTemplate.postForObject("/api/auth/authenticate",
                new AuthRequest("admin", "password"),
                AuthResponse.class).token();
    }
}
