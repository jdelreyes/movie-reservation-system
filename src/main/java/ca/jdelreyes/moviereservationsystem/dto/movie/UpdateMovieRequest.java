package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import jakarta.validation.constraints.NotEmpty;

public record UpdateMovieRequest(@NotEmpty String title,
                                 @NotEmpty String description,
                                 @NotEmpty String director,
                                 @NotEmpty Genre genre) {
}
