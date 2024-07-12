package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long>, PagingAndSortingRepository<Movie, Long> {
    @Query("SELECT DISTINCT(m) FROM MovieSchedule ms JOIN ms.movie m WHERE " +
            "ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<Movie> findAvailableMovies(Pageable pageable);
}

