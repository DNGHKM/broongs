package com.broongs.dto.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCarRequestDTO {
    private Long teamId;
    private String number;
    private String model;
    private String color;
    private Long mileage;
    private Integer fuelLevel;
    private MultipartFile carImage;
}