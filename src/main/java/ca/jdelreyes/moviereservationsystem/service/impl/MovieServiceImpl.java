package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.helper.Mapper;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieScheduleRepository movieScheduleRepository;

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
    public MovieResponse updateMovie(Long id, UpdateMovieRequest updateMovieRequest) throws NotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException::new);
        movie = setMovie(movie.getId(), updateMovieRequest);

        movieRepository.save(movie);

        return Mapper.mapMovieToMovieResponse(movie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) throws NotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException::new);

        movieScheduleRepository.deleteAllByMovie(movie);
        movieRepository.delete(movie);
    }

    private Movie setMovie(Long id, UpdateMovieRequest updateMovieRequest) {
        return Movie.builder()
                .id(id)
                .title(updateMovieRequest.title())
                .description(updateMovieRequest.description())
                .director(updateMovieRequest.director())
                .genre(updateMovieRequest.genre())
                .build();
    }
}
