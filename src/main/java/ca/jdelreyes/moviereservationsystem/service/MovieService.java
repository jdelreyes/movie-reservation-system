package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.exception.ForbiddenException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {
    List<MovieResponse> getMovies(PageRequest pageRequest);

    MovieImageResponse getMovieImage(Long movieId) throws NotFoundException;

    MovieResponse getMovie(Long id) throws NotFoundException;

    MovieImageResponse uploadMovieImage(Long movieId, MultipartFile multipartFile) throws IOException, NotFoundException, ForbiddenException;

    MovieResponse createMovie(CreateMovieRequest createMovieRequest);

    MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest) throws NotFoundException;

    void deleteMovieImage(Long id) throws NotFoundException;

    void deleteMovie(Long id) throws NotFoundException;
}
