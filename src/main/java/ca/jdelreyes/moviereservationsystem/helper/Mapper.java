package ca.jdelreyes.moviereservationsystem.helper;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.model.*;

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
                theater.getLocation()
        );
    }

    public static TheaterDetailsResponse mapTheaterToTheaterDetailsResponse(Theater theater, List<Seat> seatList) {
        return new TheaterDetailsResponse(
                theater.getId(),
                theater.getName(),
                theater.getLocation(),
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

    public static MovieScheduleResponse mapMovieScheduleToMovieScheduleResponse(MovieSchedule movieSchedule) {
        return new MovieScheduleResponse(
                movieSchedule.getId(),
                movieSchedule.getStartTime(),
                movieSchedule.getEndTime(),
                movieSchedule.getMovie(),
                movieSchedule.getTheater(),
                movieSchedule.getIsCancelled()
        );
    }

    public static Seat mapCreateSeatRequestToSeat(Theater theater, CreateSeatRequest createSeatRequest) {
        return Seat.builder()
                .seatNumber(createSeatRequest.seatNumber())
                .rowLetter(createSeatRequest.rowLetter())
                .theater(theater)
                .isReserved(true)
                .build();
    }

    public static Seat mapUpdateSeatRequestToSeat(Theater theater, UpdateSeatRequest updateSeatRequest) {
        return Seat.builder()
                .seatNumber(updateSeatRequest.seatNumber())
                .rowLetter(updateSeatRequest.rowLetter())
                .theater(theater)
                .isReserved(true)
                .build();
    }
}
