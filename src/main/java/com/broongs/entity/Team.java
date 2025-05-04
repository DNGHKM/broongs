package com.broongs.entity;

import com.broongs.dto.team.UpdateTeamRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    protected Team() {
    }

    public static Team makeNewTeam(String teamName) {
        return Team.builder()
                .name(teamName)
                .build();
    }

    public void deleteTeam() {
        this.deleted = true;
    }

    public void updateTeam(UpdateTeamRequestDTO dto) {
        this.name = dto.getTeamName();
    }
}
