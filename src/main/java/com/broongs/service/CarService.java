package com.broongs.service;

import com.broongs.dto.car.*;
import com.broongs.entity.Car;
import com.broongs.entity.Team;
import com.broongs.enums.Role;
import com.broongs.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final TeamService teamService;
    private final FileService fileService;
    private final CarRepository carRepository;
    private final String FILE_DIR = "/car";

    public CarInfoResponseDTO getCarInfo(String email, Long id) {
        Car car = carRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("차량 정보 조회 불가능"));
        teamService.validateAccessToTeam(email, car.getTeam().getId());

        return CarInfoResponseDTO.from(car);
    }

    public AddCarResponseDTO addCar(String email, AddCarRequestDTO dto) {
        validateManagePermission(email, dto.getTeamId());
        validateCarNumberDuplicate(dto.getNumber(), dto.getTeamId());

        Team team = teamService.validateAndGetTeam(email, dto.getTeamId());

        String fileUUID = uploadCarImage(dto.getCarImage(), null);
        Car car = carRepository.save(Car.addCar(team, dto, fileUUID));
        return AddCarResponseDTO.from(car);
    }

    public UpdateCarResponseDTO updateCar(Long id, String email, UpdateCarRequestDTO dto) {
        Car car = carRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("차량 없음"));
        Long teamId = car.getTeam().getId();

        validateManagePermission(email, teamId);
        validateCarNumberDuplicate(dto.getNumber(), teamId);

        String fileUUID = uploadCarImage(dto.getCarImage(), car.getImageUUID());

        car.update(dto, fileUUID);
        return UpdateCarResponseDTO.from(car);
    }

    public List<CarListResponseDTO> getCarListByTeam(Long teamId, String email) {
        Team team = teamService.validateAndGetTeam(email, teamId);
        return carRepository.findCarsByTeam(team).stream()
                .map(car -> CarListResponseDTO.from(car, teamId))
                .collect(Collectors.toList());
    }

    public void deleteCar(Long id, String email) {
        Car car = carRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("차량 없음"));
        validateManagePermission(email, car.getTeam().getId());
        car.delete();
    }

    private void validateManagePermission(String email, Long teamId) {
        Role role = teamService.getUserRoleOfTeam(email, teamId);
        if (role != Role.OWNER && role != Role.MANAGER) {
            throw new RuntimeException("권한이 없습니다.");
        }
    }

    private void validateCarNumberDuplicate(String number, @NotNull Long teamId) {
        carRepository.findCarByNumberAndTeamId(number, teamId)
                .ifPresent(c -> {
                    throw new RuntimeException("차량 번호 중복입니다.");
                });
    }

    private String uploadCarImage(MultipartFile carImage, String oldUUID) {
        if (ObjectUtils.isEmpty(carImage)) return null;
        if (oldUUID != null) {
            fileService.deleteFile(FILE_DIR, oldUUID);
        }
        return fileService.uploadFile(carImage, FILE_DIR);
    }
}
