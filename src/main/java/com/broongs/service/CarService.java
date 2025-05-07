package com.broongs.service;

import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.AddCarResponseDTO;
import com.broongs.dto.car.UpdateCarRequestDTO;
import com.broongs.dto.car.UpdateCarResponseDTO;
import com.broongs.entity.Car;
import com.broongs.entity.Team;
import com.broongs.enums.Role;
import com.broongs.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final TeamService teamService;
    private final FileService fileService;
    private final CarRepository carRepository;
    private final String FILE_DIR = "/car";

    public AddCarResponseDTO addCar(String email, AddCarRequestDTO dto) {
        Role role = teamService.getUserRoleOfTeam(email, dto.getTeamId());
        if (role != Role.OWNER && role != Role.MANAGER) {
            throw new RuntimeException("권한이 없습니다.");
        }
        Team team = teamService.validateUserHasAccessAndGetTeam(email, dto.getTeamId());
        carRepository.findCarByNumber(dto.getNumber())
                .ifPresent(c -> {
                    throw new RuntimeException("차량 번호 중복입니다.");
                });

        String fileUUID = null;
        if (!ObjectUtils.isEmpty(dto.getCarImage())) {
            fileUUID = fileService.uploadFile(dto.getCarImage(), FILE_DIR);
        }
        Car car = carRepository.save(Car.addCar(team, dto, fileUUID));
        return new AddCarResponseDTO(car.getId(), car.getNumber(), car.getTeam().getName());
    }

    public UpdateCarResponseDTO updateCar(Long id, String email, UpdateCarRequestDTO dto) {
        Role role = teamService.getUserRoleOfTeam(email, dto.getTeamId());
        if (role != Role.OWNER && role != Role.MANAGER) {
            throw new RuntimeException("권한이 없습니다.");
        }
        Team team = teamService.validateUserHasAccessAndGetTeam(email, dto.getTeamId());

        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("차량 없음"));

        String fileUUID = null;
        if (!ObjectUtils.isEmpty(dto.getCarImage())) {
            fileService.deleteFile(FILE_DIR, car.getImageUUID());
            fileUUID = fileService.uploadFile(dto.getCarImage(), FILE_DIR);
        }

        car.update(dto, team, fileUUID);
        return new UpdateCarResponseDTO(car.getId(), car.getNumber(), car.getTeam().getName());
    }
}
