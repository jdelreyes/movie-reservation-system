package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Seat;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByTheater(Theater theater);
}
