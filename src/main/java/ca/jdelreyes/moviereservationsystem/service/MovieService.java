package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getMovies(PageRequest pageRequest);

    MovieResponse getMovie(Long id) throws NotFoundException;

    MovieResponse createMovie(CreateMovieRequest createMovieRequest);

    MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest) throws NotFoundException;

    void deleteMovie(Long id) throws NotFoundException;
}
