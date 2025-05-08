package com.broongs.service;

import com.broongs.dto.car.ParkInfoResponseDTO;
import com.broongs.dto.parking.ParkingRequestDTO;
import com.broongs.dto.parking.ParkingResponseDTO;
import com.broongs.entity.Car;
import com.broongs.entity.Park;
import com.broongs.entity.User;
import com.broongs.repository.ParkRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkService {
    private final CarService carService;
    private final UserService userService;
    private final FileService fileService;
    private final TeamService teamService;
    private final ParkRepository parkRepository;
    private final String FILE_DIR = "/park";

    public ParkingResponseDTO parking(Long carId, @Valid ParkingRequestDTO dto, String email) {
        Car car = carService.findUndeletedCarById(carId);
        User user = userService.findUserByEmail(email);
        teamService.validateAccessToTeam(email, car.getTeam().getId());

        String imageUUID = null;
        if (!ObjectUtils.isEmpty(dto.getParkImage())) {
            imageUUID = fileService.uploadFile(dto.getParkImage(), FILE_DIR);
        }

        Park park = Park.parking(user, car, dto, imageUUID);
        parkRepository.save(park);
        return ParkingResponseDTO.from(park);
    }

    @Transactional(readOnly = true)
    public List<ParkInfoResponseDTO> getParkingListByCarId(Long carId, String email) {
        Car car = carService.findUndeletedCarById(carId);
        teamService.validateAccessToTeam(email, car.getTeam().getId());
        List<Park> parkList = parkRepository.findParksByCar(car);
        return parkList.stream().map(ParkInfoResponseDTO::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParkInfoResponseDTO getParkInfo(Long parkId, String email) {
        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("주차 정보가 없습니다."));
        carService.validateAccessCar(park.getCar().getId(), email);
        return ParkInfoResponseDTO.from(park);
    }

    public void deletePark(Long parkId, String email) {
        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("주차 정보가 없습니다."));
        Car car = park.getCar();
        carService.validateAccessCar(car.getId(), email);

        parkRepository.delete(park);
    }
}
