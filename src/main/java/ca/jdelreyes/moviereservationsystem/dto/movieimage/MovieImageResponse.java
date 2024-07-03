package ca.jdelreyes.moviereservationsystem.dto.movieimage;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MovieImageResponse(Long id, String name, String type,
                                 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt, byte[] data) {
}
