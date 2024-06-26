package ca.jdelreyes.moviereservationsystem.model;

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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isCancelled;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Movie movie;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Theater theater;

    @PrePersist
    private void checkIfStartTimeIsAfterEndTime() {
        if (startTime.isAfter(endTime))
            throw new RuntimeException();
    }
}
