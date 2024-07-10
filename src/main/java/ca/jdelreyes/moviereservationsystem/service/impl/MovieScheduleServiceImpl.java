package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieImage;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.repository.MovieImageRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.repository.TheaterRepository;
import ca.jdelreyes.moviereservationsystem.service.MovieScheduleService;
import ca.jdelreyes.moviereservationsystem.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// todo:
//  need to implement movie schedule filters based on parameters such as theater, date

@Service
@RequiredArgsConstructor
@Transactional
public class MovieScheduleServiceImpl implements MovieScheduleService {
    private final TheaterRepository theaterRepository;
    private final MovieRepository movieRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final MovieImageRepository movieImageRepository;

    @Override
    public MovieScheduleResponse airMovie(
            CreateMovieScheduleRequest createMovieScheduleRequest
    ) throws NotFoundException {
        Theater theater =
                theaterRepository.findById(createMovieScheduleRequest.theaterId()).orElseThrow(NotFoundException::new);
        Movie movie =
                movieRepository.findById(createMovieScheduleRequest.movieId()).orElseThrow(NotFoundException::new);
        MovieImage movieImage =
                movieImageRepository.findByMovie(movie);

        MovieSchedule movieSchedule = MovieSchedule.builder()
                .startDateTime(createMovieScheduleRequest.startDateTime())
                .endDateTime(createMovieScheduleRequest.endDateTime())
                .ticketPurchaseOpeningDateTime(createMovieScheduleRequest.ticketPurchaseOpeningDateTime())
                .ticketPurchaseClosingDateTime(createMovieScheduleRequest.ticketPurchaseClosingDateTime())
                .movie(movie)
                .isCancelled(false)
                .theater(theater)
                .build();

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule, movieImage);
    }

    @Override
    public MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository
                .findById(movieScheduleId)
                .orElseThrow(NotFoundException::new);
        MovieImage movieImage =
                movieImageRepository.findById(movieSchedule.getMovie().getId()).get();

        movieSchedule.setIsCancelled(true);

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule, movieImage);
    }

    @Override
    public MovieScheduleResponse rescheduleMovie(
            Long movieScheduleId,
            RescheduleMovieRequest rescheduleMovieRequest
    ) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository
                .findById(movieScheduleId)
                .orElseThrow(NotFoundException::new);
        MovieImage movieImage = movieImageRepository.findByMovie(movieSchedule.getMovie());

        movieSchedule.setStartDateTime(rescheduleMovieRequest.startDateTime());
        movieSchedule.setEndDateTime(rescheduleMovieRequest.endDateTime());
        movieSchedule.setTicketPurchaseOpeningDateTime(rescheduleMovieRequest.ticketPurchaseOpeningDateTime());
        movieSchedule.setTicketPurchaseClosingDateTime(rescheduleMovieRequest.ticketPurchaseClosingDateTime());

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule, movieImage);
    }

    @Override
    public List<MovieScheduleResponse> getTheaterMovieSchedules(Long theaterId) throws NotFoundException {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(NotFoundException::new);
        List<MovieSchedule> movieScheduleList = movieScheduleRepository.findAllByTheater(theater);

        return movieScheduleList
                .stream()
                .map(movieSchedule ->
                        Mapper.mapMovieScheduleToMovieScheduleResponse(
                                movieSchedule, movieImageRepository.findByMovie(movieSchedule.getMovie())
                        ))
                .toList();
    }

    @Override
    public List<MovieScheduleResponse> getMovieMovieSchedules(Movie movie) throws NotFoundException {
        return List.of();
    }

    @Override
    public MovieScheduleResponse getMovieSchedule(Long id) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository.findById(id).orElseThrow(NotFoundException::new);
        MovieImage movieImage = movieImageRepository.findByMovie(movieSchedule.getMovie());

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule, movieImage);
    }
}
