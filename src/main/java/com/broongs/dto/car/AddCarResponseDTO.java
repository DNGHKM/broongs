package com.broongs.dto.car;

import com.broongs.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCarResponseDTO {
    private Long id;
    private String number;
    private String teamName;

    public static AddCarResponseDTO from(Car car){
        return new AddCarResponseDTO(car.getId(), car.getNumber(), car.getTeam().getName());
    }
}
