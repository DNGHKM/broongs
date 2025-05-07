package com.broongs.dto.car;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarRequestDTO {
    @NotNull
    private String number;

    private String model;

    private String color;

    @Min(0)
    private Long mileage;

    @Min(0)
    @Max(100)
    private Integer fuelLevel;

    private MultipartFile carImage;
}