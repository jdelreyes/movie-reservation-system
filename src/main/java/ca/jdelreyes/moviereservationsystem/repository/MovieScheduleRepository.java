package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, Long> {
}
