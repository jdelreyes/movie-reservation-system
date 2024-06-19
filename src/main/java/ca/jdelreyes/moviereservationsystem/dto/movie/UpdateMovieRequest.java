package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateMovieRequest(@NotEmpty String title,
                                 @NotEmpty String description,
                                 @NotEmpty String director,
                                 @NotNull Genre genre) {
}
