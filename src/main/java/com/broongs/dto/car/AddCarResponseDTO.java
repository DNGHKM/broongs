package com.broongs.dto.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCarResponseDTO {
    private Long id;
    private String number;
    private String teamName;
}
