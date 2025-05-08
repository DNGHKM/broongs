package com.broongs.dto.parking;

import com.broongs.entity.Location;
import com.broongs.entity.Park;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ParkingResponseDTO {
    private Long parkId;

    private Long carId;

    private String comment;

    private Location location;

    private LocalDateTime time;

    public static ParkingResponseDTO from(Park park) {
        return new ParkingResponseDTO(park.getId(),
                park.getCar().getId(),
                park.getComment(),
                park.getLocation(),
                park.getTime());
    }
}
