package ca.jdelreyes.moviereservationsystem.util;

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

    public static MovieResponse mapMovieToMovieResponse(Movie movie, MovieImage movieImage) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDirector(),
                movie.getGenre(),
                ImageUtil.decompressImage(movieImage.getData())
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

    public static MovieScheduleResponse mapMovieScheduleToMovieScheduleResponse(MovieSchedule movieSchedule, MovieImage movieImage) {
        return new MovieScheduleResponse(
                movieSchedule.getId(),
                movieSchedule.getStartDateTime(),
                movieSchedule.getEndDateTime(),
                movieSchedule.getTicketPurchaseOpeningDateTime(),
                movieSchedule.getTicketPurchaseClosingDateTime(),
                mapMovieToMovieResponse(movieSchedule.getMovie(), movieImage),
                mapTheaterToTheaterResponse(movieSchedule.getTheater()),
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
                movieSchedule.getStartDateTime(),
                movieSchedule.getEndDateTime());
    }

    public static MovieImageResponse mapMovieImageToMovieImageResponse(MovieImage movieImage) {
        return new MovieImageResponse(
                movieImage.getId(),
                movieImage.getName(),
                movieImage.getType(),
                movieImage.getCreatedAt(),
                ImageUtil.decompressImage(movieImage.getData())
        );
    }
}
