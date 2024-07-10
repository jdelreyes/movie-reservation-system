package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.model.enums.Genre;

import java.util.List;
import java.util.Set;

public record MovieResponse(Long id, String title, String description, List<String> directors,
                            Set<Genre> genres, MovieImageResponse movieImage) {
}
