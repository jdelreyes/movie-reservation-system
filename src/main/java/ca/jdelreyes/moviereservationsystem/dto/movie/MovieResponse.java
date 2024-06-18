package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;

public record MovieResponse(String title, String description, String director, Genre genre) {
}
