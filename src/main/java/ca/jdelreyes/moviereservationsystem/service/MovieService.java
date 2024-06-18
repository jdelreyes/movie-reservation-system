package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MovieService {
    public List<MovieResponse> getMovies(PageRequest pageRequest);

    public MovieResponse getMovie(Long id);

    public MovieResponse createMovie(CreateMovieRequest createMovieRequest);

    public MovieResponse updateMovie();//todo
}
