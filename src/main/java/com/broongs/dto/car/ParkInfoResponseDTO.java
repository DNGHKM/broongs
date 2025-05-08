package com.broongs.dto.car;

import com.broongs.entity.Location;
import com.broongs.entity.Park;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ParkInfoResponseDTO {
    private Long id;
    private String userNickname;
    private String comment;
    private Location location;
    private LocalDateTime time;
    private String imageUUID;

    public static ParkInfoResponseDTO from(Park park) {
        return new ParkInfoResponseDTO(park.getId(),
                park.getUser().getNickname(),
                park.getComment(),
                park.getLocation(),
                park.getTime(),
                park.getImageUUID());
    }
}
