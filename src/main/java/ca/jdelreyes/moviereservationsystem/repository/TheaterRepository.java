package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Theater;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends CrudRepository<Theater, Long>, PagingAndSortingRepository<Theater, Long> {
}
