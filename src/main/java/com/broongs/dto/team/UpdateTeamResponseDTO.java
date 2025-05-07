package com.broongs.dto.team;

import com.broongs.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTeamResponseDTO {
    private Long id;
    private String teamName;

    public static UpdateTeamResponseDTO from(Team team) {
        return new UpdateTeamResponseDTO(team.getId(), team.getName());
    }
}
