package com.broongs.dto.team;

import com.broongs.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MakeTeamResponseDTO {
    private Long id;
    private String teamName;

    public static MakeTeamResponseDTO from(Team team) {
        return new MakeTeamResponseDTO(team.getId(), team.getName());
    }
}
