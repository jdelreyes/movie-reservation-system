package ca.jdelreyes.moviereservationsystem.helper;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.Seat;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import ca.jdelreyes.moviereservationsystem.model.User;

import java.util.List;

public class Mapper {
    private Mapper() {
    }

    public static UserResponse mapUserToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }

    public static MovieResponse mapMovieToMovieResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDirector(),
                movie.getGenre()
        );
    }

    public static TheaterResponse mapTheaterToTheaterResponse(Theater theater) {
        return new TheaterResponse(
                theater.getId(),
                theater.getName(),
                theater.getLocation(),
                theater.getCapacity()
        );
    }

    public static TheaterDetailsResponse mapTheaterToTheaterDetailsResponse(Theater theater, List<Seat> seatList) {
        return new TheaterDetailsResponse(
                theater.getId(),
                theater.getName(),
                theater.getLocation(),
                theater.getCapacity(),
                seatList.stream().map(Mapper::mapSeatToSeatResponse).toList()
        );
    }

    public static SeatResponse mapSeatToSeatResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getRowLetter(),
                seat.getSeatNumber(),
                seat.getIsReserved()
        );
    }
}
