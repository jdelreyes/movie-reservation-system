package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieSchedule;
import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieScheduleRepository extends
        CrudRepository<MovieSchedule, Long>, PagingAndSortingRepository<MovieSchedule, Long> {
    void deleteAllByTheater(Theater theater);

    void deleteAllByMovie(Movie movie);

    // todo: test
    @Query("SELECT ms FROM MovieSchedule ms " +
            "WHERE ms.theater = :theater " +
            "AND ms.ticketPurchaseOpeningDateTime <= CURRENT_TIMESTAMP " +
            "AND ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<MovieSchedule> findAvailableSchedulesForTheater(@Param("theater") Theater theater);

    @Query("SELECT ms FROM MovieSchedule ms " +
            "WHERE ms.movie = :movie " +
            "AND ms.ticketPurchaseOpeningDateTime <= CURRENT_TIMESTAMP " +
            "AND ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<MovieSchedule> findAvailableSchedulesForMovie(@Param("movie") Movie movie);

    @Query("SELECT ms FROM MovieSchedule ms " +
            "WHERE ms.movie = :movie " +
            "AND ms.theater = :theater " +
            "AND ms.ticketPurchaseOpeningDateTime <= CURRENT_TIMESTAMP " +
            "AND ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<MovieSchedule> findAvailableSchedulesForMovieAndTheater(
            @Param("movie") Movie movie, @Param("theater") Theater theater
    );

    @Query("SELECT ms FROM MovieSchedule ms " +
            "WHERE ms.ticketPurchaseOpeningDateTime <= CURRENT_TIMESTAMP " +
            "AND ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<MovieSchedule> findAvailableMovieSchedules();
}
