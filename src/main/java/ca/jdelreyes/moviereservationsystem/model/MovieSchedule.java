package ca.jdelreyes.moviereservationsystem.model;

import ca.jdelreyes.moviereservationsystem.model.enums.MovieType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "movie_schedules")
public class MovieSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private LocalDateTime ticketPurchaseOpeningDateTime;
    private LocalDateTime ticketPurchaseClosingDateTime;

    private Boolean isCancelled;

    @Enumerated(EnumType.STRING)
    private MovieType movieType;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Movie movie;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Theater theater;

    @PrePersist
    @PreUpdate
    private void checkDateTime() {
        if (startDateTime.isAfter(endDateTime))
            throw new RuntimeException("startDateTime should not be after endDateTime");
        if (ticketPurchaseOpeningDateTime.isAfter(ticketPurchaseClosingDateTime))
            throw new RuntimeException("ticketPurchaseOpeningDateTime should not be after ticketPurchaseClosingDateTime");
        if (ticketPurchaseClosingDateTime.isAfter(startDateTime))
            throw new RuntimeException("ticketPurchaseOpeningDateTime should not be after startDateTime");
    }
}
