package com.broongs.dto.car;

import com.broongs.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarListResponseDTO {
    private Long carId;
    private Long teamId;
    private String number;
    private String imageUUID;
    private boolean available;

    public static CarListResponseDTO from(Car car, Long teamId) {
        return new CarListResponseDTO(car.getId(),
                teamId,
                car.getNumber(),
                car.getImageUUID(),
                car.isAvailable());
    }
}
