package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.theater.CreateTheaterRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.service.impl.TheaterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterServiceImpl theaterService;
    private final String NAME_FIELD = "name";

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> getTheaters(Pageable pageable) {
        return ResponseEntity.ok(theaterService.getTheaters(PageRequest.of(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, NAME_FIELD)))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDetailsResponse> getTheater(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(theaterService.getTheater(id));
    }

    @PostMapping
    public ResponseEntity<TheaterDetailsResponse> createTheater(@Valid @RequestBody CreateTheaterRequest createTheaterRequest) {
        // fixme: fix implementation
        TheaterDetailsResponse theaterResponse = theaterService.createTheater(null, null);

        return ResponseEntity.created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/api/theaters/{id}")
                        .buildAndExpand(theaterResponse.id())
                        .toUri()
        ).body(theaterResponse);
    }
}
