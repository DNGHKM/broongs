package com.broongs.dto.car;

import com.broongs.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarInfoResponseDTO {
    private Long id;
    private String number;
    private String model;
    private String color;
    private Long mileage;
    private Integer fuelLevel;
    private String imageUUID;
    private boolean available;

    public static CarInfoResponseDTO from(Car car){
        return new CarInfoResponseDTO(car.getId(),
                car.getNumber(),
                car.getModel(),
                car.getColor(),
                car.getMileage(),
                car.getFuelLevel(),
                car.getImageUUID(),
                car.isAvailable());
    }
}
