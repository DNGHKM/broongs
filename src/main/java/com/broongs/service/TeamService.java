package com.broongs.service;

import com.broongs.dto.team.*;
import com.broongs.entity.Team;
import com.broongs.entity.User;
import com.broongs.entity.UserTeam;
import com.broongs.enums.Role;
import com.broongs.repository.TeamRepository;
import com.broongs.repository.UserRepository;
import com.broongs.repository.UserTeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;

    public List<UserTeamListResponseDTO> getUserTeamList(String username) {
        User user = getUser(username);
        return teamRepository.getUserTeamList(user.getId()).stream()
                .map(t -> new UserTeamListResponseDTO(t.getId(), t.getName()))
                .collect(Collectors.toList());
    }

    public MakeTeamResponseDTO addTeam(String email, MakeTeamRequestDTO dto) {
        Team team = Team.makeNewTeam(dto.getTeamName());
        User user = getUser(email);
        UserTeam userTeam = UserTeam.madeByOwner(user, team);

        teamRepository.save(team);
        userTeamRepository.save(userTeam);

        return new MakeTeamResponseDTO(team.getId(), team.getName(), email);
    }

    public DeleteTeamResponseDTO deleteTeam(String email, Long teamId) {
        User user = getUser(email);
        if (userTeamRepository.findUserRole(teamId, user.getId()) != Role.OWNER) {
            throw new RuntimeException("권한없음");
        }
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀 없음"));
        team.deleteTeam();
        return new DeleteTeamResponseDTO(team.getId());
    }

    public UpdateTeamResponseDTO updateTeam(Long teamId, UpdateTeamRequestDTO dto, String email) {
        User user = getUser(email);
        if (userTeamRepository.findUserRole(teamId, user.getId()) != Role.OWNER) {
            throw new RuntimeException("권한없음");
        }
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("팀 없음"));
        String beforeTeamName = team.getName();
        team.updateTeam(dto);
        return new UpdateTeamResponseDTO(team.getId(), beforeTeamName, team.getName());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저 못찾음"));
    }
}
