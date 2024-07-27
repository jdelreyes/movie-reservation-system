package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.movieschedule.CreateMovieScheduleRequest;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.RescheduleMovieRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.service.impl.MovieScheduleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movie-schedules")
@RequiredArgsConstructor
public class MovieScheduleController {
    private final MovieScheduleServiceImpl movieScheduleService;

    @GetMapping
    public ResponseEntity<List<MovieScheduleResponse>> getMovieSchedules(
            @RequestParam(name = "theater") Optional<Long> theaterId,
            @RequestParam(name = "movie") Optional<Long> movieId,
            @RequestParam(name = "date") Optional<Instant> instant
    ) throws NotFoundException {
        List<MovieScheduleResponse> schedules;

        if (theaterId.isPresent() && movieId.isPresent() && instant.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByTheaterAndMovieAndLocalDateTime(
                    theaterId.get(),
                    movieId.get(),
                    LocalDateTime.ofInstant(instant.get(), ZoneId.of("America/Toronto")));
        } else if (theaterId.isPresent() && instant.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByTheaterAndLocalDateTime(theaterId.get(),
                    LocalDateTime.ofInstant(instant.get(), ZoneId.of("America/Toronto")));
        } else if (movieId.isPresent() && instant.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByMovieAndLocalDateTime(movieId.get(),
                    LocalDateTime.ofInstant(instant.get(), ZoneId.of("America/Toronto")));
        } else if (theaterId.isPresent() && movieId.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByTheaterAndMovie(theaterId.get(), movieId.get());
        } else if (theaterId.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByTheater(theaterId.get());
        } else if (movieId.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByMovie(movieId.get());
        } else if (instant.isPresent()) {
            schedules = movieScheduleService.getAvailableMovieSchedulesByLocalDateTime(
                    LocalDateTime.ofInstant(instant.get(), ZoneId.of("America/Toronto"))
            );
        } else {
            schedules = movieScheduleService.getAvailableMovieSchedules();
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("{id}")
    public ResponseEntity<MovieScheduleResponse> getMovieSchedule(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(movieScheduleService.getMovieSchedule(id));
    }


    @PostMapping
    public ResponseEntity<MovieScheduleResponse> airMovie(
            @Valid @RequestBody CreateMovieScheduleRequest createMovieScheduleRequest
    ) throws NotFoundException {
        MovieScheduleResponse movieScheduleResponse = movieScheduleService.airMovie(createMovieScheduleRequest);

        return ResponseEntity
                .created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(movieScheduleResponse.id())
                        .toUri())
                .body(movieScheduleResponse);
    }

    @PutMapping("/cancel-movie/{movieScheduleId}")
    public ResponseEntity<MovieScheduleResponse> cancelMovie(
            @PathVariable("movieScheduleId") Long movieScheduleId
    ) throws NotFoundException {
        return ResponseEntity.ok(movieScheduleService.cancelMovie(movieScheduleId));
    }

    @PutMapping("/reschedule-movie/{movieScheduleId}")
    public ResponseEntity<MovieScheduleResponse> rescheduleMovie(
            @PathVariable("movieScheduleId") Long movieScheduleId,
            @Valid @RequestBody RescheduleMovieRequest rescheduleMovieRequest
    ) throws NotFoundException {
        return ResponseEntity.ok(movieScheduleService.rescheduleMovie(movieScheduleId, rescheduleMovieRequest));
    }
}
