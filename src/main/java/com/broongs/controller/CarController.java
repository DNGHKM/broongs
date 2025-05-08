package com.broongs.controller;

import com.broongs.dto.ApiResponse;
import com.broongs.dto.car.*;
import com.broongs.dto.parking.ParkingRequestDTO;
import com.broongs.dto.parking.ParkingResponseDTO;
import com.broongs.service.CarService;
import com.broongs.service.ParkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final ParkService parkService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCarInfo(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            CarInfoResponseDTO carInfoDTO = carService.getCarInfo(userDetails.getUsername(), id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("차량을 조회하였습니다. ", carInfoDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 조회에 실패하였습니다. " + e.getMessage()));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> addCar(@ModelAttribute @Valid AddCarRequestDTO dto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            AddCarResponseDTO addCarResponseDTO = carService.addCar(userDetails.getUsername(), dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("차량을 등록하였습니다. ", addCarResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 등록에 실패하였습니다. " + e.getMessage()));
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateCar(@PathVariable Long id,
                                                 @ModelAttribute @Valid UpdateCarRequestDTO dto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UpdateCarResponseDTO updateCarResponseDTO = carService.updateCar(id, userDetails.getUsername(), dto);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("차량을 수정하였습니다. ", updateCarResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 수정에 실패하였습니다. " + e.getMessage()));
        }
    }

    @PatchMapping(value = "/{id}/available")
    public ResponseEntity<ApiResponse> updateCarAvailability(@PathVariable Long id,
                                                             @RequestBody @Valid UpdateCarAvailableRequestDTO dto,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            carService.validateManageCar(id, dto, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("차량 사용가능 상태를 업데이트 하였습니다. ", id));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 사용 가능 상태 변경 실패 " + e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse> deleteCar(@PathVariable Long id,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            carService.deleteCar(id, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("차량을 삭제하였습니다. ", id));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("차량 삭제를 실패하였습니다. " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/parkings")
    public ResponseEntity<ApiResponse> registerParking(@PathVariable Long id,
                                                       @ModelAttribute @Valid ParkingRequestDTO dto,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            ParkingResponseDTO parkingResponseDTO = parkService.parking(id, dto, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("주차정보를 등록하였습니다. ", parkingResponseDTO));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("주차정보 등록에 실패하였습니다. " + e.getMessage()));
        }
    }


    @GetMapping("/{id}/parkings")
    public ResponseEntity<ApiResponse> getParkingListByCarId(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<ParkInfoResponseDTO> parkInfoResponseDTOs = parkService.getParkingListByCarId(id, userDetails.getUsername());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success("주차정보 목록을 조회하였습니다. ", parkInfoResponseDTOs));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("주차정보 목록 조회에 실패하였습니다. " + e.getMessage()));
        }
    }
}
