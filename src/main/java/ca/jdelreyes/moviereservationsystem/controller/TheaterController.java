package ca.jdelreyes.moviereservationsystem.controller;

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
import ca.jdelreyes.moviereservationsystem.service.impl.TheaterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterServiceImpl theaterService;

    @PutMapping("/air-movie")
    public ResponseEntity<MovieScheduleResponse> airMovie(
            @Valid @RequestBody CreateMovieScheduleRequest createMovieScheduleRequest
    ) throws NotFoundException {
        return ResponseEntity.ok(theaterService.airMovie(createMovieScheduleRequest));
    }

    @PutMapping("/cancel-movie")
    public ResponseEntity<MovieScheduleResponse> cancelMovie(Long movieScheduleId) throws NotFoundException {
        return ResponseEntity.ok(theaterService.cancelMovie(movieScheduleId));
    }

    @PutMapping("/reschedule-movie")
    public ResponseEntity<MovieScheduleResponse> rescheduleMovie(
            RescheduleMovieRequest rescheduleMovieRequest
    ) throws NotFoundException {
        return ResponseEntity.ok(theaterService.rescheduleMovie(rescheduleMovieRequest));
    }

    @PostMapping("/add-seats")
    public ResponseEntity<List<SeatResponse>> addTheaterSeats(
            Long theaterId, List<CreateSeatRequest> createSeatRequestList
    ) throws NotFoundException {
        return new ResponseEntity<>(
                theaterService.addTheaterSeats(theaterId, createSeatRequestList), HttpStatus.CREATED
        );
    }

    @PutMapping("/edit-seats")
    public ResponseEntity<List<SeatResponse>> editTheaterSeats(
            Long theaterId, List<UpdateSeatRequest> updateSeatRequestList
    ) throws NotFoundException {
        return ResponseEntity.ok(theaterService.editTheaterSeats(theaterId, updateSeatRequestList));
    }

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> getTheaters(Pageable pageable) {
        final String NAME_FIELD = "name";

        return ResponseEntity.ok(theaterService.getTheaters(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, NAME_FIELD)))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDetailsResponse> getTheater(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(theaterService.getTheater(id));
    }

    @PostMapping
    public ResponseEntity<TheaterResponse> createTheater(@Valid @RequestBody CreateTheaterRequest createTheaterRequest) {
        TheaterResponse theaterResponse = theaterService.createTheater(createTheaterRequest);

        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(theaterResponse.id())
                        .toUri()
        ).body(theaterResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<TheaterResponse> updateTheater(
            @PathVariable("id") Long id, @Valid @RequestBody UpdateTheaterRequest updateTheaterRequest
    ) throws NotFoundException {
        return ResponseEntity.ok(theaterService.updateTheater(id, updateTheaterRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable("id") Long id) throws NotFoundException {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
