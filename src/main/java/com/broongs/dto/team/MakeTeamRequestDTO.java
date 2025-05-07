package com.broongs.dto.team;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MakeTeamRequestDTO {
    @NotNull
    private String teamName;
}
