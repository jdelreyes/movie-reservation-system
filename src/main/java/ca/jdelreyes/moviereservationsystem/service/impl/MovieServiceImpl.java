package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movie.CreateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movie.UpdateMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.exception.ForbiddenException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieImage;
import ca.jdelreyes.moviereservationsystem.repository.MovieImageRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.service.MovieService;
import ca.jdelreyes.moviereservationsystem.util.ImageUtil;
import ca.jdelreyes.moviereservationsystem.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final MovieImageRepository movieImageRepository;

    @Override
    public List<MovieResponse> getAvailableMovies(PageRequest pageRequest) {
        return movieRepository.findAvailableMovies(pageRequest).stream().map(Mapper::mapMovieToMovieResponse).toList();
    }

    @Override
    public MovieImageResponse getMovieImage(Long movieId) throws NotFoundException {
        MovieImage movieImage = movieImageRepository
                .findById(movieId)
                .orElseThrow(NotFoundException::new);

        return Mapper.mapMovieImageToMovieImageResponse(movieImage);
    }

    @Override
    public MovieResponse getMovie(Long id) throws NotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException::new);

        return Mapper.mapMovieToMovieResponse(movie);
    }

    @Override
    public MovieImageResponse uploadMovieImage(
            Long movieId, MultipartFile multipartFile
    ) throws IOException, NotFoundException, ForbiddenException {
        if (!isFileImageType(multipartFile)) throw new ForbiddenException();

        Movie movie = movieRepository.findById(movieId).orElseThrow(NotFoundException::new);

        MovieImage movieImage = movieImageRepository.save(
                MovieImage.builder()
                        .name(multipartFile.getName())
                        .type(multipartFile.getContentType())
                        .data(ImageUtil.compressImage(multipartFile.getBytes()))
                        .movie(movie)
                        .build()
        );

        return Mapper.mapMovieImageToMovieImageResponse(movieImage);
    }

    @Override
    public MovieResponse createMovie(CreateMovieRequest createMovieRequest) throws IOException {
        FileSystemResource imageResourcePlaceholder = createPlaceholderImage();

        Movie movie = movieRepository.save(Movie.builder()
                .title(createMovieRequest.title())
                .description(createMovieRequest.description())
                .director(createMovieRequest.director())
                .genre(createMovieRequest.genre())
                .build());

        movieImageRepository.save(MovieImage.builder()
                .movie(movie)
                .data(imageResourcePlaceholder.getContentAsByteArray())
                .type("image/jpeg")
                .name(imageResourcePlaceholder.getFilename())
                .build());

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
    public void deleteMovieImage(Long id) throws NotFoundException {
        MovieImage movieImage = movieImageRepository.findById(id).orElseThrow(NotFoundException::new);

        movieImageRepository.delete(movieImage);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) throws NotFoundException {
        Movie movie = movieRepository.findById(id).orElseThrow(NotFoundException::new);

        movieScheduleRepository.deleteAllByMovie(movie);
        movieImageRepository.deleteByMovie(movie);
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

    private boolean isFileImageType(MultipartFile multipartFile) {
        assert multipartFile.getContentType() != null;

        Pattern pattern = Pattern.compile("image/.*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(multipartFile.getContentType());

        return matcher.find();
    }

    private FileSystemResource createPlaceholderImage() {
        String pathName = "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                "images" + File.separator +
                "placeholder.jpg";

        File file = new File(pathName);
        return new FileSystemResource(file);
    }
}
