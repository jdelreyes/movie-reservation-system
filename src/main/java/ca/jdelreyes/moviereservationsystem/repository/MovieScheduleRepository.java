package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, Long> {
    List<MovieSchedule> findAllByTheater(Theater theater);

    void deleteAllByTheater(Theater theater);

    void deleteAllByMovie(Movie movie);
}
