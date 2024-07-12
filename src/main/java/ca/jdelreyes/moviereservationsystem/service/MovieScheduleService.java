package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;

import java.util.List;

public interface MovieScheduleService {
    MovieScheduleResponse airMovie(CreateMovieScheduleRequest createMovieScheduleRequest) throws NotFoundException;

    MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException;

    MovieScheduleResponse rescheduleMovie(Long movieScheduleId, RescheduleMovieRequest rescheduleMovieRequest) throws NotFoundException;

    List<MovieScheduleResponse> getTheaterMovieSchedules(Long theaterId) throws NotFoundException;

    List<MovieScheduleResponse> getMovieMovieSchedules(Long movieId) throws NotFoundException;

    List<MovieScheduleResponse> getTheaterAndMovieMovieSchedules(Long theaterId, Long movieId) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedules();

    MovieScheduleResponse getMovieSchedule(Long id) throws NotFoundException;
}
