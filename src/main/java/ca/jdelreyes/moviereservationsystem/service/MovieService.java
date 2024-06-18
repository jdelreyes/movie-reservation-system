package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MovieService {
    public List<MovieResponse> getMovies(PageRequest pageRequest);

    public MovieResponse getMovie(Long id) throws NotFoundException;

    public MovieResponse createMovie(CreateMovieRequest createMovieRequest);

    public MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest);//todo

    public void deleteMovie(Long id);
}
