package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;

public record UpdateMovieRequest(@NotEmpty String title,
                                 @NotEmpty String description,
                                 @NotEmpty List<String> directors,
                                 @NotEmpty Set<Genre> genres) {
}
