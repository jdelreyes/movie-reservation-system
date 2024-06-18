package ca.jdelreyes.moviereservationsystem.helper;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.User;

public class Mapper {
    private Mapper() {
    }

    public static UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    public static MovieResponse mapMovieToMovieResponse(Movie movie) {
        return new MovieResponse(movie.getTitle(), movie.getDescription(), movie.getDirector(), movie.getGenre());
    }
}
