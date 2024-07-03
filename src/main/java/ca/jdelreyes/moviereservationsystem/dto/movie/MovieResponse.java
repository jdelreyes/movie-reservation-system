package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;

public record MovieResponse(Long id, String title, byte[] imageData, String description, String director, Genre genre) {
}
