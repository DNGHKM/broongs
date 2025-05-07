package com.broongs.service;

import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.AddCarResponseDTO;
import com.broongs.dto.car.CarInfoResponseDTO;
import com.broongs.dto.car.UpdateCarRequestDTO;
import com.broongs.entity.Car;
import com.broongs.entity.Team;
import com.broongs.enums.Role;
import com.broongs.repository.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private TeamService teamService;
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarService carService;

    @Test
    @DisplayName("차량 조회 - 성공")
    void getCarInfo_success() {
        //given
        String email = "test@test.com";
        Long carId = 1L;

        Team team = Team.builder()
                .id(2L)
                .name("팀1")
                .deleted(false)
                .build();

        Car car = Car.builder()
                .id(carId)
                .team(team)
                .number("123가1234")
                .model("싼타페")
                .color("흰색")
                .mileage(10000L)
                .fuelLevel(50)
                .imageUUID("image.jpg")
                .available(true)
                .team(team)
                .build();

        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));
        doNothing().when(teamService).validateAccessToTeam(email, car.getTeam().getId());
        //when
        CarInfoResponseDTO carInfo = carService.getCarInfo(email, carId);

        //then
        assertEquals(carInfo.getId(), car.getId());
        assertEquals(carInfo.getNumber(), car.getNumber());
    }

    @Test
    @DisplayName("차량 조회 - 실패(팀 권한 없음)")
    void getCarInfo_fail_invalid_team() {
        //given
        String email = "test@test.com";
        Long carId = 1L;

        Team team = Team.builder()
                .id(2L)
                .name("팀1")
                .deleted(false)
                .build();

        Car car = Car.builder()
                .id(carId)
                .team(team)
                .number("123가1234")
                .model("싼타페")
                .color("흰색")
                .mileage(10000L)
                .fuelLevel(50)
                .imageUUID("image.jpg")
                .available(true)
                .team(team)
                .build();

        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));
        doThrow(new RuntimeException("권한 없음"))
                .when(teamService).validateAccessToTeam(email, car.getTeam().getId());


        //when & then
        assertThrows(RuntimeException.class, () -> carService.getCarInfo(email, carId));
    }

    @Test
    @DisplayName("차량 등록 - 성공")
    void carAdd_success() {
        //given
        String email = "test@test.com";
        Long teamId = 1L;

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(teamId,
                "123가1234",
                "싼타페",
                "흰색",
                12000L,
                60,
                null);
        Team team = Team.builder().id(teamId)
                .name("팀1")
                .deleted(false).build();
        when(teamService.validateAndGetTeam(email, requestDTO.getTeamId())).thenReturn(team);
        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumberAndTeamId(requestDTO.getNumber(), team.getId())).thenReturn(Optional.empty());

        Car car = Car.addCar(team, requestDTO, null);
        when(carRepository.save(ArgumentMatchers.any(Car.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        AddCarResponseDTO responseDTO = carService.addCar(email, requestDTO);

        //then
        assertEquals(car.getId(), responseDTO.getId());
        assertEquals(requestDTO.getNumber(), responseDTO.getNumber());

        verify(teamService).validateAndGetTeam(email, requestDTO.getTeamId());
        verify(carRepository).findCarByNumberAndTeamId(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("차량 등록 - 실패(이미 동일 팀에 같은 번호 있음)")
    void carAdd_fail_duplicate_number() {
        //given
        String email = "test@test.com";

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(10L,
                "123가1234",
                "싼타페",
                "흰색",
                12000L,
                60,
                null);

        Team team = Team.builder().id(10L)
                .name("팀1")
                .deleted(false).build();

        Car duplicatedCar = Car.builder()
                .id(99L)
                .team(team)
                .number(requestDTO.getNumber())
                .build();


        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumberAndTeamId(requestDTO.getNumber(), team.getId())).thenReturn(Optional.of(duplicatedCar));

        // when/then
        assertThrows(RuntimeException.class, () -> carService.addCar(email, requestDTO));
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    @DisplayName("차량 등록 - 실패(소속 팀이 아님)")
    void carAdd_fail_no_team() {
        //given
        String email = "test@test.com";

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(1L,
                "123가1234",
                "싼타페",
                "흰색",
                12000L,
                60,
                null);

        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(null);

        // when/then
        assertThrows(RuntimeException.class, () -> carService.addCar(email, requestDTO));
    }

    @Test
    @DisplayName("차량 등록 - 실패(권한이 없음)")
    void carAdd_fail_invalid_role() {
        //given
        String email = "test@test.com";

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(1L,
                "123가1234",
                "싼타페",
                "흰색",
                12000L,
                60,
                null);

        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MEMBER);

        // when/then
        assertThrows(RuntimeException.class, () -> carService.addCar(email, requestDTO));
        verifyNoMoreInteractions(carRepository);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    @DisplayName("차량 수정 - 성공")
    void carUpdate_success() {
        //given
        Long carId = 1L;
        String email = "test@test.com";

        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(
                "222나2222",
                "모델2",
                "색상2",
                2L,
                2,
                null);

        Team team = Team.builder()
                .id(10L)
                .name("팀1")
                .deleted(false)
                .build();

        Car car = Car.builder()
                .id(carId)
                .team(team)
                .number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null)
                .deleted(false)
                .build();

        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));
        when(teamService.getUserRoleOfTeam(email, car.getTeam().getId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumberAndTeamId(requestDTO.getNumber(), car.getTeam().getId())).thenReturn(Optional.empty());

        //when
        carService.updateCar(carId, email, requestDTO);

        //then
        assertEquals(car.getNumber(), requestDTO.getNumber());
        assertEquals(car.getModel(), requestDTO.getModel());
        assertEquals(car.getColor(), requestDTO.getColor());
        assertEquals(car.getMileage(), requestDTO.getMileage());
        assertEquals(car.getFuelLevel(), requestDTO.getFuelLevel());
    }

    @Test
    @DisplayName("차량 수정 - 실패(권한이 없음)")
    void carUpdate_fail_invalid_role() {
        //given
        Long carId = 1L;
        String email = "test@test.com";
        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(
                "222나2222",
                "모델2",
                "색상2",
                2L,
                2,
                null);

        Team team = Team.builder()
                .id(10L)
                .name("팀1")
                .deleted(false)
                .build();

        Car car = Car.builder()
                .id(1L)
                .team(team)
                .number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null).build();

        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));
        when(teamService.getUserRoleOfTeam(email, car.getTeam().getId())).thenReturn(Role.MEMBER);

        //when & then
        assertThrows(RuntimeException.class,
                () -> carService.updateCar(carId, email, requestDTO));

        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("차량 수정 - 실패(수정하려 하는 번호가 중복됨)")
    void carUpdate_fail_duplicate_number() {
        //given
        Long carId = 1L;
        String email = "test@test.com";
        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(
                "222나2222",
                "모델2",
                "색상2",
                -1L,
                110,
                null);

        Team team = Team.builder()
                .id(10L)
                .name("팀1")
                .deleted(false)
                .build();

        Car car = Car.builder()
                .id(1L)
                .team(team)
                .number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null).build();

        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));
        when(teamService.getUserRoleOfTeam(email, car.getTeam().getId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumberAndTeamId(requestDTO.getNumber(), car.getTeam().getId()))
                .thenReturn(Optional.of(mock(Car.class)));

        //when & then
        assertThrows(RuntimeException.class,
                () -> carService.updateCar(carId, email, requestDTO));

        verifyNoMoreInteractions(teamService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("차량 삭제 - 성공")
    void deleteCar_success() {
        //given
        Long carId = 1L;
        String email = "test@test.com";
        Team team = Team.builder()
                .id(1L)
                .name("팀1")
                .build();

        Car car = Car.builder()
                .team(team)
                .id(carId)
                .number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null).build();


        when(teamService.getUserRoleOfTeam(email, car.getTeam().getId())).thenReturn(Role.MANAGER);
        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));

        //when
        carService.deleteCar(carId, email);

        //then
        assertTrue(car.isDeleted());
    }

    @Test
    @DisplayName("차량 삭제 - 실패(권한없음)")
    void deleteCar_fail_invalid_role() {
        //given
        Long carId = 1L;
        String email = "test@test.com";
        Team team = Team.builder()
                .id(1L)
                .name("팀1")
                .build();

        Car car = Car.builder()
                .team(team)
                .id(carId)
                .number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null).build();


        when(teamService.getUserRoleOfTeam(email, car.getTeam().getId())).thenReturn(Role.MEMBER);
        when(carRepository.findByIdAndDeletedFalse(carId)).thenReturn(Optional.of(car));

        //when & then
        assertThrows(RuntimeException.class, () -> carService.deleteCar(carId, email));
        assertFalse(car.isDeleted());
    }
}