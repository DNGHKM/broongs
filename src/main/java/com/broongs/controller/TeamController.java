package com.broongs.controller;

import com.broongs.dto.ApiResponse;
import com.broongs.dto.car.CarListResponseDTO;
import com.broongs.dto.team.*;
import com.broongs.service.CarService;
import com.broongs.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;
    private final CarService carService;

    @GetMapping
    public ResponseEntity<ApiResponse> getUserTeamList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<UserTeamListResponseDTO> userTeamList = teamService.getUserTeamList(userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("유저가 소속된 팀 목록을 조회하였습니다.", userTeamList));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("팀 목록 조회에 실패하였습니다."));
        }
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<ApiResponse> getCarListByTeam(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<CarListResponseDTO> carListResponseDTO = carService.getCarListByTeam(id, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("팀 소속의 차량 목록을 조회합니다.", carListResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 목록 조회에 실패하였습니다." + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> makeTeam(@RequestBody @Valid MakeTeamRequestDTO dto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            MakeTeamResponseDTO makeTeamResponseDTO = teamService.addTeam(userDetails.getUsername(), dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("팀을 생성했습니다.", makeTeamResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("팀 생성을 실패했습니다."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTeam(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            DeleteTeamResponseDTO deleteTeamResponseDTO
                    = teamService.deleteTeam(userDetails.getUsername(), id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("팀을 삭제했습니다.", deleteTeamResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("팀 삭제를 실패했습니다."));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTeam(@PathVariable Long id,
                                                  @RequestBody @Valid UpdateTeamRequestDTO dto,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UpdateTeamResponseDTO updateTeamResponseDTO = teamService.updateTeam(id, dto, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("팀을 수정했습니다.", updateTeamResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("팀 수정을 실패했습니다."));
        }
    }
}
