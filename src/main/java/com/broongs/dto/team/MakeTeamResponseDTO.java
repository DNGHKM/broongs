package com.broongs.dto.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MakeTeamResponseDTO {
    private Long id;
    private String teamName;
    private String OwnerEmail;
}
