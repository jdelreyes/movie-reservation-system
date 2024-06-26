package ca.jdelreyes.moviereservationsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "theaters")
public class Theater implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<MovieSchedule> movieScheduleList;
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<Seat> seatList;
}
