package ca.jdelreyes.moviereservationsystem.utils;

import ca.jdelreyes.moviereservationsystem.dto.movie.MovieResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieimage.MovieImageResponse;
import ca.jdelreyes.moviereservationsystem.dto.movieschedule.MovieScheduleResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.CreateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.seat.UpdateSeatRequest;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterDetailsResponse;
import ca.jdelreyes.moviereservationsystem.dto.theater.TheaterResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.repository.MovieImageDataRepository;

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
                movieSchedule.getMovieType(),
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

    public static TicketResponse mapTicketToTicketResponse(Ticket ticket, Seat seat, MovieSchedule movieSchedule) {
        return new TicketResponse(ticket.getId(),
                ticket.getPrice(),
                ticket.getPurchaseTime(),
                seat.getRowLetter(),
                seat.getSeatNumber(),
                movieSchedule.getTheater().getName(),
                movieSchedule.getTheater().getLocation(),
                movieSchedule.getStartTime(),
                movieSchedule.getEndTime());
    }

    public static MovieImageResponse mapMovieImageDataToMovieImageResponse(MovieImageData movieImageData) {
        return new MovieImageResponse(movieImageData.getId(),
                movieImageData.getName(),
                movieImageData.getType(),
                movieImageData.getCreatedAt(),
                movieImageData.getData());
    }
}
