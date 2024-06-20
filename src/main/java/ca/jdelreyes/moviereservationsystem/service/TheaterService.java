package ca.jdelreyes.moviereservationsystem.service;

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
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TheaterService {
    MovieScheduleResponse airMovie(CreateMovieScheduleRequest createMovieScheduleRequest) throws NotFoundException;

    MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException;

    MovieScheduleResponse rescheduleMovie(RescheduleMovieRequest rescheduleMovieRequest) throws NotFoundException;

    List<TheaterResponse> getTheaters(PageRequest pageRequest);

    TheaterDetailsResponse getTheater(Long id) throws NotFoundException;

    TheaterDetailsResponse createTheater(CreateTheaterRequest createTheaterRequest,
                                         List<CreateSeatRequest> createSeatRequestList);

    TheaterDetailsResponse updateTheater(Long id, UpdateTheaterRequest updateTheaterRequest,
                                         List<UpdateSeatRequest> updateSeatRequestList) throws NotFoundException;

    void deleteTheater(Long id) throws NotFoundException;
}
