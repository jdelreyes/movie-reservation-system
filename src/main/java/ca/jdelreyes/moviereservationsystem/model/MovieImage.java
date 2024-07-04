package ca.jdelreyes.moviereservationsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movie_images")
@EntityListeners(AuditingEntityListener.class)
public class MovieImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Movie movie;

    private String name;
    private String type;

    @CreatedDate
    private LocalDateTime createdAt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;
}
