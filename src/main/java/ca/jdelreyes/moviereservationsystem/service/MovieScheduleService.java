package ca.jdelreyes.moviereservationsystem.service;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MovieScheduleService {
    MovieScheduleResponse airMovie(CreateMovieScheduleRequest createMovieScheduleRequest) throws NotFoundException;

    MovieScheduleResponse cancelMovie(Long movieScheduleId) throws NotFoundException;

    MovieScheduleResponse rescheduleMovie(
            Long movieScheduleId, RescheduleMovieRequest rescheduleMovieRequest
    ) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByTheater(Long theaterId) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByMovie(Long movieId) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByTheaterAndMovie(
            Long theaterId, Long movieId
    ) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByTheaterAndLocalDateTime(
            Long theaterId, LocalDateTime localDateTime
    ) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByMovieAndLocalDateTime(
            Long movieId, LocalDateTime localDateTime
    ) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByTheaterAndMovieAndLocalDateTime(
            Long theaterId, Long movieId, LocalDateTime localDateTime
    ) throws NotFoundException;

    List<MovieScheduleResponse> getAvailableMovieSchedulesByLocalDateTime(LocalDateTime localDateTime);

    List<MovieScheduleResponse> getAvailableMovieSchedules();


    MovieScheduleResponse getMovieSchedule(Long id) throws NotFoundException;
}
