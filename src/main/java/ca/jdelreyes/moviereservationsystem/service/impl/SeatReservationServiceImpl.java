package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.seat.SeatResponse;
import ca.jdelreyes.moviereservationsystem.dto.ticket.CreateTicketRequest;
import ca.jdelreyes.moviereservationsystem.dto.ticket.TicketResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import ca.jdelreyes.moviereservationsystem.util.Mapper;
import ca.jdelreyes.moviereservationsystem.model.*;
import ca.jdelreyes.moviereservationsystem.repository.MovieScheduleRepository;
import ca.jdelreyes.moviereservationsystem.repository.SeatRepository;
import ca.jdelreyes.moviereservationsystem.repository.TheaterRepository;
import ca.jdelreyes.moviereservationsystem.repository.TicketRepository;
import ca.jdelreyes.moviereservationsystem.service.SeatReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatReservationServiceImpl implements SeatReservationService {
    private final TicketRepository ticketRepository;
    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final MovieScheduleRepository movieScheduleRepository;

    @Override
    public List<SeatResponse> getTheaterSeats(Long theaterId) throws NotFoundException {
        Theater theater = theaterRepository
                .findById(theaterId)
                .orElseThrow(NotFoundException::new);

        List<Seat> seatList = seatRepository.findAllByTheater(theater);

        return seatList.stream().map(Mapper::mapSeatToSeatResponse).toList();
    }

    @Override
    public TicketResponse getOwnTicket(User user, Long ticketId) throws NotFoundException {
        Ticket ticket = ticketRepository.findByIdAndUser(ticketId, user).orElseThrow(NotFoundException::new);

        return Mapper.mapTicketToTicketResponse(ticket, ticket.getSeat(), ticket.getMovieSchedule());
    }

    @Override
    public TicketResponse buyTicket(User user, CreateTicketRequest createTicketRequest) throws NotFoundException {
        MovieSchedule movieSchedule = movieScheduleRepository
                .findById(createTicketRequest.movieScheduleId())
                .orElseThrow(NotFoundException::new);

        Seat seat = seatRepository
                .findById(createTicketRequest.seatId())
                .orElseThrow(NotFoundException::new);

        seat.setIsReserved(true);
        seatRepository.save(seat);

        Ticket ticket = Ticket.builder()
                .movieSchedule(movieSchedule)
                .seat(seat)
                .user(user)
                .price(MovieType.valueOf(movieSchedule.getMovieType().name()).price)
                .build();

        ticketRepository.save(ticket);

        return Mapper.mapTicketToTicketResponse(ticket, seat, movieSchedule);
    }

    @Override
    @Transactional
    public void refundTicket(User user, Long ticketId) throws NotFoundException {
        Ticket ticket = ticketRepository
                .findById(ticketId)
                .orElseThrow(NotFoundException::new);

        ticket.getSeat().setIsReserved(false);
        seatRepository.save(ticket.getSeat());

        ticketRepository.deleteByIdAndUser(ticketId, user);
    }
}
