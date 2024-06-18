package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.helper.Mapper;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public List<MovieResponse> getMovies(PageRequest pageRequest) {
        return movieRepository.findAll(pageRequest).stream().map(Mapper::mapMovieToMovieResponse).toList();
    }

    @Override
    public MovieResponse getMovie(Long id) throws NotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException::new);

        return Mapper.mapMovieToMovieResponse(movie);
    }

    @Override
    public MovieResponse createMovie(CreateMovieRequest createMovieRequest) {
        Movie movie = Movie.builder()
                .title(createMovieRequest.title())
                .description(createMovieRequest.description())
                .director(createMovieRequest.director())
                .genre(createMovieRequest.genre())
                .build();

        movieRepository.save(movie);

        return Mapper.mapMovieToMovieResponse(movie);
    }

    @Override
    public MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest) {
        return null; //todo
    }

    @Override
    public void deleteMovie(Long id) {

    }
}
