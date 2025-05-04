package com.broongs.dto.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTeamResponseDTO {
    private Long id;
    private String beforeTeamName;
    private String afterTeamName;
}
