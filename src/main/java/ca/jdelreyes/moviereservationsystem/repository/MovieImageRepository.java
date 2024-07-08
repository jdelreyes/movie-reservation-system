package ca.jdelreyes.moviereservationsystem.repository;

import ca.jdelreyes.moviereservationsystem.model.Movie;
import ca.jdelreyes.moviereservationsystem.model.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {
    MovieImage findByMovie(Movie movie);

    void deleteByMovie(Movie movie);
}
