package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.UpdateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TheaterService {
    MovieScheduleResponse airMovie(CreateMovieScheduleRequest createMovieScheduleRequest) throws NotFoundException;

    MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException;

    MovieScheduleResponse rescheduleMovie(Long movieScheduleId, RescheduleMovieRequest rescheduleMovieRequest) throws NotFoundException;

    List<TheaterResponse> getTheaters(PageRequest pageRequest);

    TheaterDetailsResponse getTheater(Long id) throws NotFoundException;

    TheaterResponse createTheater(CreateTheaterRequest createTheaterRequest);

    List<SeatResponse> addTheaterSeats(
            Long theaterId, List<CreateSeatRequest> createSeatRequestList
    ) throws NotFoundException;

    TheaterResponse updateTheater(Long id, UpdateTheaterRequest updateTheaterRequest) throws NotFoundException;

    List<SeatResponse> editTheaterSeats(
            Long theaterId, List<UpdateSeatRequest> updateSeatRequestList
    ) throws NotFoundException;

    void deleteTheater(Long id) throws NotFoundException;
}
