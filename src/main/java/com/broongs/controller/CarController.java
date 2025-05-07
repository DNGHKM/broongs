package com.broongs.controller;

import com.broongs.dto.ApiResponse;
import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.AddCarResponseDTO;
import com.broongs.dto.car.UpdateCarRequestDTO;
import com.broongs.dto.car.UpdateCarResponseDTO;
import com.broongs.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {
    private final CarService carService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addCar(@ModelAttribute @Valid AddCarRequestDTO dto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            AddCarResponseDTO addCarResponseDTO = carService.addCar(userDetails.getUsername(), dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("차량을 등록하였습니다.", addCarResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 등록에 실패하였습니다." + e.getMessage()));
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateCar(@PathVariable Long id,
                                                 @ModelAttribute @Valid UpdateCarRequestDTO dto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UpdateCarResponseDTO updateCarResponseDTO = carService.updateCar(id, userDetails.getUsername(), dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("차량을 수정하였습니다.", updateCarResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 수정에 실패하였습니다." + e.getMessage()));
        }
    }
}
