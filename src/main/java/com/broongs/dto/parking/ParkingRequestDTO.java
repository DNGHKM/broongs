package com.broongs.dto.parking;

import com.broongs.entity.Location;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRequestDTO {
    private String comment;

    private Location location;

    @NotNull
    private LocalDateTime time;

    private MultipartFile parkImage;
}