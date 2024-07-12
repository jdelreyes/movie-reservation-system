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
            @RequestParam(name = "movie") Optional<Long> movieId
    ) throws NotFoundException {
        if (theaterId.isPresent() && movieId.isPresent())
            return ResponseEntity.ok(movieScheduleService.getTheaterAndMovieMovieSchedules(theaterId.get(), movieId.get()));
        if (theaterId.isPresent())
            return ResponseEntity.ok(movieScheduleService.getTheaterMovieSchedules(theaterId.get()));
        if (movieId.isPresent())
            return ResponseEntity.ok(movieScheduleService.getMovieMovieSchedules(movieId.get()));

        return ResponseEntity.ok(movieScheduleService.getAvailableMovieSchedules());
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
