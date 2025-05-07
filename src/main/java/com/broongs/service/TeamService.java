package com.broongs.service;

import com.broongs.dto.team.*;
import com.broongs.entity.Team;
import com.broongs.entity.User;
import com.broongs.entity.UserTeam;
import com.broongs.enums.Role;
import com.broongs.repository.TeamRepository;
import com.broongs.repository.UserTeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserService userService;

    public List<UserTeamListResponseDTO> getUserTeamList(String username) {
        User user = userService.findUserByEmail(username);
        return teamRepository.getUserTeamList(user.getId()).stream()
                .map(t -> new UserTeamListResponseDTO(t.getId(), t.getName()))
                .collect(Collectors.toList());
    }

    public MakeTeamResponseDTO addTeam(String email, MakeTeamRequestDTO dto) {
        Team team = Team.makeNewTeam(dto.getTeamName());
        User user = userService.findUserByEmail(email);
        UserTeam userTeam = UserTeam.madeByOwner(user, team);

        teamRepository.save(team);
        userTeamRepository.save(userTeam);

        return MakeTeamResponseDTO.from(team);
    }

    public DeleteTeamResponseDTO deleteTeam(String email, Long teamId) {
        User user = userService.findUserByEmail(email);
        if (userTeamRepository.findUserRole(teamId, user.getId()) != Role.OWNER) {
            throw new RuntimeException("권한없음");
        }
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀 없음"));
        team.delete();
        return new DeleteTeamResponseDTO(team.getId());
    }

    public UpdateTeamResponseDTO updateTeam(Long teamId, UpdateTeamRequestDTO dto, String email) {
        User user = userService.findUserByEmail(email);
        if (userTeamRepository.findUserRole(teamId, user.getId()) != Role.OWNER) {
            throw new RuntimeException("권한없음");
        }
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀 없음"));
        team.updateTeam(dto);
        return UpdateTeamResponseDTO.from(team);
    }

    public void validateAccessToTeam(String email, Long teamId) {
        if (!teamRepository.existsByUserEmailAndTeamId(email, teamId)) {
            throw new RuntimeException("팀 접근 권한이 없습니다.");
        }
    }

    public Team validateAndGetTeam(String email, Long teamId) {
        return teamRepository.findAccessibleTeam(email, teamId)
                .orElseThrow(() -> new EntityNotFoundException("권한이 존재하지 않거나 팀이 없습니다."));
    }

    public Role getUserRoleOfTeam(String email, Long teamId) {
        return teamRepository.findUserRoleOfTeam(email, teamId);
    }
}
