package com.broongs.controller;

import com.broongs.dto.ApiResponse;
import com.broongs.dto.car.ParkInfoResponseDTO;
import com.broongs.service.ParkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parkings")
public class ParkController {
    private final ParkService parkService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getParkInfo(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ParkInfoResponseDTO parkInfoResponseDTO = parkService.getParkInfo(id, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("주차정보를 조회하였습니다. ", parkInfoResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("주차정보 등록에 실패하였습니다. " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePark(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            parkService.deletePark(id, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("주차정보를 삭제하였습니다. ", id));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("주차정보 등록에 실패하였습니다. " + e.getMessage()));
        }
    }
}
