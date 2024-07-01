package ca.jdelreyes.moviereservationsystem.dto.movieimage;

import java.time.LocalDateTime;

public record MovieImageResponse(Long id, String name, String type, LocalDateTime createdAt, byte[] data) {
}
