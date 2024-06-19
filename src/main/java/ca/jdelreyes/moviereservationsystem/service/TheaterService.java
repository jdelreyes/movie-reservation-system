package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.UpdateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TheaterService {
    public MovieScheduleResponse airMovie(Theater theater, Movie movie);

    public MovieScheduleResponse cancelMovie(MovieSchedule movieSchedule);

    public MovieScheduleResponse rescheduleMovie(MovieSchedule movieSchedule);

    public List<TheaterResponse> getTheaters(PageRequest pageRequest);

    public TheaterDetailsResponse getTheater(Long id);

    public TheaterResponse createTheater(CreateTheaterRequest createTheaterRequest,
                                         List<CreateSeatRequest> createSeatRequestList);

    public TheaterResponse updateTheater(Long id, UpdateTheaterRequest updateTheaterRequest,
                                         List<UpdateSeatRequest> updateSeatRequestList);

    public void deleteTheater(Long id);
}
