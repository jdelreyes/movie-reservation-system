package ca.jdelreyes.moviereservationsystem.dto.movie;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;

// todo: MovieResponse should come with image data
public record MovieResponse(Long id, String title, String description, String director, Genre genre, byte[] imageData) {
}
