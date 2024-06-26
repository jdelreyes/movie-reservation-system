package ca.jdelreyes.moviereservationsystem.dto.ticket;

import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(@NotNull Long seatId,
                                  @NotNull Long movieScheduleId) {
}
