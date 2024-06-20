package ca.jdelreyes.moviereservationsystem.dto.movieschedule;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public record RescheduleMovieRequest(@NotEmpty Long movieScheduleId, @Future LocalDateTime startTime,
                                     @Future LocalDateTime endTime) {
}
