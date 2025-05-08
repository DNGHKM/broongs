package com.broongs.dto.car;

import com.broongs.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCarResponseDTO {
    private Long id;
    private String number;
    private String teamName;

    public static UpdateCarResponseDTO from(Car car) {
        return new UpdateCarResponseDTO(car.getId(), car.getNumber(), car.getTeam().getName());
    }
}
