package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.service.impl.MovieServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieServiceImpl movieService;

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getMovies(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "title"))
        )));
    }

    @GetMapping("/{imageId}/image")
    public ResponseEntity<MovieImageResponse> getMovieImage(@PathVariable("imageId") Long imageId) throws NotFoundException {
        return ResponseEntity.ok(movieService.getMovieImage(imageId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<MovieImageResponse> uploadImage(
            @PathVariable("id") Long movieId,
            @NotNull @RequestParam("image") MultipartFile multipartFile
    ) throws IOException, NotFoundException {
        MovieImageResponse movieImageResponse = movieService.uploadMovieImage(movieId, multipartFile);

        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .build()
                        .toUri())
                .body(movieImageResponse);
    }

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest) {
        MovieResponse movieResponse = movieService.createMovie(createMovieRequest);

        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(movieResponse.id())
                        .toUri())
                .body(movieResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable("id") Long id,
                                                     @Valid @RequestBody UpdateMovieRequest updateMovieRequest) throws NotFoundException {
        return ResponseEntity.ok(movieService.updateMovie(id, updateMovieRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable("id") Long id) throws NotFoundException {
        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }
}
