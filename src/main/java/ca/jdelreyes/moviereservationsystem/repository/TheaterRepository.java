package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends CrudRepository<Theater, Long>, PagingAndSortingRepository<Theater, Long> {
    @Query("SELECT DISTINCT t FROM MovieSchedule ms JOIN ms.theater t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND ms.ticketPurchaseClosingDateTime >= CURRENT_TIMESTAMP " +
            "AND ms.isCancelled = false")
    List<Theater> findByNameContaining(@Param("name") String name);
}
