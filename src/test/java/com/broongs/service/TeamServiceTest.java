package com.broongs.service;

import com.broongs.dto.team.MakeTeamRequestDTO;
import com.broongs.dto.team.MakeTeamResponseDTO;
import com.broongs.dto.team.UpdateTeamRequestDTO;
import com.broongs.dto.team.UserTeamListResponseDTO;
import com.broongs.entity.Team;
import com.broongs.entity.User;
import com.broongs.enums.Role;
import com.broongs.repository.TeamRepository;
import com.broongs.repository.UserTeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamService teamService;

    @Test
    @DisplayName("팀 정상 생성")
    void addTeam() {
        // given
        String email = "test@example.com";
        MakeTeamRequestDTO dto = new MakeTeamRequestDTO("개발팀");

        User mockUser = User.builder().id(1L).email(email).build();

        // 스텁
        when(userService.findUserByEmail(email)).thenReturn(mockUser);
        when(teamRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userTeamRepository.save(ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        MakeTeamResponseDTO teamResponseDTO = teamService.addTeam(email, dto);

        // then
        assertEquals(dto.getTeamName(), teamResponseDTO.getTeamName());
        verify(userService).findUserByEmail(email);
        verify(teamRepository).save(ArgumentMatchers.any());
        verify(teamRepository).save(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("팀 정상 삭제")
    void deleteTeam_success() {
        // given
        String email = "test@example.com";
        Long teamId = 1L;
        User mockUser = User.builder().id(1L).email(email).build();
        Team mockTeam = Team.makeNewTeam("개발팀");

        when(userService.findUserByEmail(email)).thenReturn(mockUser);
        when(userTeamRepository.findUserRole(teamId, mockUser.getId())).thenReturn(Role.OWNER);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));

        // when
        teamService.deleteTeam(email, teamId);

        //then
        assertTrue(mockTeam.isDeleted());
    }

    @Test
    @DisplayName("팀 삭제 오류 - 권한 없음")
    void deleteTeam_fail_1() {
        // given
        String email = "test@example.com";
        Long teamId = 1L;
        User mockUser = User.builder().id(1L).email(email).build();

        when(userService.findUserByEmail(email)).thenReturn(mockUser);
        when(userTeamRepository.findUserRole(teamId, mockUser.getId())).thenReturn(Role.MEMBER);

        // when & then
        assertThrows(RuntimeException.class, () -> teamService.deleteTeam(email, teamId));
    }

    @Test
    @DisplayName("팀 정상 수정")
    void updateTeam_success() {
        // given
        String email = "test@example.com";
        Long teamId = 1L;
        User mockUser = User.builder().id(1L).email(email).build();
        Team mockTeam = Team.makeNewTeam("개발팀");
        UpdateTeamRequestDTO dto = new UpdateTeamRequestDTO("개발팀-수정");

        when(userService.findUserByEmail(email)).thenReturn(mockUser);
        when(userTeamRepository.findUserRole(teamId, mockUser.getId()))
                .thenReturn(Role.OWNER);
        when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(mockTeam));

        // when
        teamService.updateTeam(teamId, dto, email);

        //then
        assertEquals(mockTeam.getName(), dto.getTeamName());
    }

    @Test
    @DisplayName("유저 팀 목록 조회 성공")
    void getUserTeamList_success() {
        // given
        String email = "test@example.com";
        Long userId = 1L;
        User mockUser = User.builder().id(userId).email(email).build();

        List<Team> mockTeamList = List.of(
                Team.builder().id(100L).name("팀A").build(),
                Team.builder().id(101L).name("팀B").build()
        );

        when(userService.findUserByEmail(email)).thenReturn(mockUser);
        when(teamRepository.getUserTeamList(userId)).thenReturn(mockTeamList);

        // when
        List<UserTeamListResponseDTO> result = teamService.getUserTeamList(email);

        // then
        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).getId());
        assertEquals("팀A", result.get(0).getTeamName());
        assertEquals(101L, result.get(1).getId());
        assertEquals("팀B", result.get(1).getTeamName());

        verify(userService).findUserByEmail(email);
        verify(teamRepository).getUserTeamList(userId);
    }

    @DisplayName("유저 팀 목록 조회 실패 - 유저 없음")
    @Test
    void getUserTeamList_fail_userNotFound() {
        // given
        String email = "nonexistent@example.com";
        when(userService.findUserByEmail(email)).thenReturn(null);

        // when & then
        assertThrows(NullPointerException.class, () ->
                teamService.getUserTeamList(email)
        );

        verify(userService).findUserByEmail(email);
        verifyNoMoreInteractions(teamRepository);
    }
}
