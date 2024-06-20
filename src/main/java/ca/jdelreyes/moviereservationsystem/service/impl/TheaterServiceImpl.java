package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.UpdateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.helper.Mapper;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Seat;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.repository.MovieRepository;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.repository.SeatRepository;
import ca.jdelreyes.moviereservationsystem.repository.TheaterRepository;
import ca.jdelreyes.moviereservationsystem.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final MovieRepository movieRepository;

    @Override
    public MovieScheduleResponse airMovie(CreateMovieScheduleRequest createMovieScheduleRequest) throws NotFoundException {
        Theater theater =
                theaterRepository.findById(createMovieScheduleRequest.theaterId()).orElseThrow(NotFoundException::new);
        Movie movie =
                movieRepository.findById(createMovieScheduleRequest.movieId()).orElseThrow(NotFoundException::new);

        MovieSchedule movieSchedule = MovieSchedule.builder()
                .startTime(createMovieScheduleRequest.startTime())
                .endTime(createMovieScheduleRequest.endTime())
                .movie(movie)
                .isCancelled(false)
                .theater(theater)
                .build();

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule);
    }

    @Override
    public MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository
                .findById(movieScheduleId)
                .orElseThrow(NotFoundException::new);

        movieSchedule.setIsCancelled(true);

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule);
    }

    @Override
    public MovieScheduleResponse rescheduleMovie(RescheduleMovieRequest rescheduleMovieRequest) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository
                .findById(rescheduleMovieRequest.movieScheduleId())
                .orElseThrow(NotFoundException::new);

        movieSchedule.setStartTime(rescheduleMovieRequest.startTime());
        movieSchedule.setEndTime(rescheduleMovieRequest.endTime());

        movieScheduleRepository.save(movieSchedule);

        return Mapper.mapMovieScheduleToMovieScheduleResponse(movieSchedule);
    }

    @Override
    public List<TheaterResponse> getTheaters(PageRequest pageRequest) {
        return theaterRepository.findAll(pageRequest).stream().map(Mapper::mapTheaterToTheaterResponse).toList();
    }

    @Override
    public TheaterDetailsResponse getTheater(Long id) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);
        List<Seat> seatList = seatRepository.findAllByTheater(theater);
        return Mapper.mapTheaterToTheaterDetailsResponse(theater, seatList);
    }

    @Override
    public TheaterDetailsResponse createTheater(CreateTheaterRequest createTheaterRequest,
                                                List<CreateSeatRequest> createSeatRequestList) {
        Theater theater = Theater.builder()
                .name(createTheaterRequest.name())
                .location(createTheaterRequest.location())
                .build();

        theaterRepository.save(theater);
        List<Seat> seatList = seatRepository.saveAll(
                createSeatRequestList
                        .stream()
                        .map((createSeatRequest -> Seat.builder()
                                .theater(theater)
                                .rowLetter(createSeatRequest.rowLetter())
                                .seatNumber(createSeatRequest.seatNumber())
                                .isReserved(false)
                                .build()))
                        .toList()
        );

        return Mapper.mapTheaterToTheaterDetailsResponse(theater, seatList);
    }

    //    fixme: updating a theater with the seats could lead to seats being reduced or added.
//           should i separate the theater creation/update and seats creation/update?
    @Override
    public TheaterDetailsResponse updateTheater(Long id,
                                                UpdateTheaterRequest updateTheaterRequest,
                                                List<UpdateSeatRequest> updateSeatRequestList) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);
        theater = setTheater(theater, updateTheaterRequest);


        return null;
    }

    @Override
    public void deleteTheater(Long id) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);

        theaterRepository.delete(theater);
    }

    private Theater setTheater(Theater theater, UpdateTheaterRequest updateTheaterRequest) {
        theater.setName(updateTheaterRequest.name());
        theater.setLocation(updateTheaterRequest.location());
        return theater;
    }
}
