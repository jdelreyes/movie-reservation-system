package ca.jdelreyes.moviereservationsystem.model;

import ca.jdelreyes.moviereservationsystem.model.enums.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String director;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Genre genre;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MovieSchedule> movieScheduleList;
}
