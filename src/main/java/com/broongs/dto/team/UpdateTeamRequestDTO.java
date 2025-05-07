package com.broongs.dto.team;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTeamRequestDTO {
    @Valid
    private String teamName;
}
