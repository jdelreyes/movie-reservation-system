package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.UpdateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.Seat;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.repository.SeatRepository;
import ca.jdelreyes.moviereservationsystem.repository.TheaterRepository;
import ca.jdelreyes.moviereservationsystem.service.TheaterService;
import ca.jdelreyes.moviereservationsystem.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService {
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final MovieScheduleRepository movieScheduleRepository;

    @Override
    public List<TheaterResponse> getTheaters(PageRequest pageRequest) {
        return theaterRepository.findAll(pageRequest).stream().map(Mapper::mapTheaterToTheaterResponse).toList();
    }

    @Override
    public List<TheaterResponse> getTheatersByNameContaining(String theaterName) {
        return theaterRepository.findByNameContaining(theaterName).stream().map(Mapper::mapTheaterToTheaterResponse).toList();
    }

    @Override
    public TheaterDetailsResponse getTheater(Long id) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);
        List<Seat> seatList = seatRepository.findAllByTheater(theater);
        return Mapper.mapTheaterToTheaterDetailsResponse(theater, seatList);
    }

    @Override
    public TheaterResponse createTheater(CreateTheaterRequest createTheaterRequest) {
        Theater theater = Theater.builder()
                .name(createTheaterRequest.name())
                .location(createTheaterRequest.location())
                .build();

        theaterRepository.save(theater);

        return Mapper.mapTheaterToTheaterResponse(theater);
    }

    @Override
    public List<SeatResponse> addTheaterSeats(
            Long theaterId, List<CreateSeatRequest> createSeatRequestList
    ) throws NotFoundException {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(NotFoundException::new);

        List<Seat> seatList = createSeatRequestList.stream()
                .map(createSeatRequest -> Mapper.mapCreateSeatRequestToSeat(theater, createSeatRequest))
                .toList();

        seatRepository.saveAll(seatList);

        return seatList.stream().map(Mapper::mapSeatToSeatResponse).toList();
    }

    @Override
    public TheaterResponse updateTheater(Long id, UpdateTheaterRequest updateTheaterRequest) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);
        theater = setTheater(theater, updateTheaterRequest);

        theaterRepository.save(theater);

        return Mapper.mapTheaterToTheaterResponse(theater);
    }

    @Override
    @Transactional
    public List<SeatResponse> editTheaterSeats(
            Long theaterId, List<UpdateSeatRequest> updateSeatRequestList
    ) throws NotFoundException {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(NotFoundException::new);
        seatRepository.deleteAllByTheater(theater);

        List<Seat> seatList = updateSeatRequestList.stream()
                .map(updateSeatRequest -> Mapper.mapUpdateSeatRequestToSeat(theater, updateSeatRequest))
                .toList();

        seatRepository.saveAll(seatList);

        return seatList.stream().map(Mapper::mapSeatToSeatResponse).toList();
    }

    @Override
    @Transactional
    public void deleteTheater(Long id) throws NotFoundException {
        Theater theater = theaterRepository.findById(id).orElseThrow(NotFoundException::new);

        movieScheduleRepository.deleteAllByTheater(theater);
        seatRepository.deleteAllByTheater(theater);
        theaterRepository.delete(theater);
    }

    private Theater setTheater(Theater theater, UpdateTheaterRequest updateTheaterRequest) {
        theater.setName(updateTheaterRequest.name());
        theater.setLocation(updateTheaterRequest.location());
        return theater;
    }
}
