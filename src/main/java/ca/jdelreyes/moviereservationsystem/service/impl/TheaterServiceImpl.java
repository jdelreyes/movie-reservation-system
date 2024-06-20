package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
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

    @Override
    public MovieScheduleResponse airMovie(Theater theater, Movie movie) {
        return null;
    }

    @Override
    public MovieScheduleResponse cancelMovie(MovieSchedule movieSchedule) {
        return null;
    }

    @Override
    public MovieScheduleResponse rescheduleMovie(MovieSchedule movieSchedule) {
        return null;
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
                .capacity(createTheaterRequest.capacity())
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
        theater.setCapacity(updateTheaterRequest.capacity());
        return theater;
    }
}
