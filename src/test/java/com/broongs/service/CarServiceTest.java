package com.broongs.service;

import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.AddCarResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("차량등록 - 성공")
    void carAdd_success() {
        //given
        String email = "test@test.com";

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(1L,
                "123가1234",
                "싼타페",
                "흰색",
                12000L,
                60,
                null);
        Team team = Team.builder().id(10L)
                .name("팀1")
                .deleted(false).build();
        when(teamService.validateUserHasAccessAndGetTeam(email, requestDTO.getTeamId())).thenReturn(team);
        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumber(requestDTO.getNumber())).thenReturn(Optional.empty());

        Car car = Car.addCar(team, requestDTO, null);
        when(carRepository.save(ArgumentMatchers.any(Car.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        AddCarResponseDTO responseDTO = carService.addCar(email, requestDTO);

        //then
        assertEquals(car.getId(), responseDTO.getId());
        assertEquals(requestDTO.getNumber(), responseDTO.getNumber());

        verify(teamService).validateUserHasAccessAndGetTeam(email, requestDTO.getTeamId());
        verify(carRepository).findCarByNumber(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("차량등록 - 실패(이미 같은 번호 있음)")
    void carAdd_fail_duplicate_number() {
        //given
        String email = "test@test.com";

        AddCarRequestDTO requestDTO = new AddCarRequestDTO(1L,
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
                .number(requestDTO.getNumber())
                .build();


        when(teamService.validateUserHasAccessAndGetTeam(email, requestDTO.getTeamId())).thenReturn(team);
        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MANAGER);
        when(carRepository.findCarByNumber(requestDTO.getNumber())).thenReturn(Optional.ofNullable(duplicatedCar));

        // when/then
        assertThrows(RuntimeException.class, () -> carService.addCar(email, requestDTO));
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("차량등록 - 실패(소속 팀이 아님)")
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
    @DisplayName("차량등록 - 실패(권한이 없음)")
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
        Long id = 1L;
        String email = "test@test.com";

        Car car = Car.builder()
                .id(1L).number("111가1111")
                .model("모델1")
                .color("색상1")
                .mileage(1L)
                .fuelLevel(1)
                .imageUUID(null).build();

        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(1L,
                "222나2222",
                "모델2",
                "색상2",
                2L,
                2,
                null);
        Team team = Team.builder().id(10L)
                .name("팀1")
                .deleted(false).build();

        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MANAGER);
        when(teamService.validateUserHasAccessAndGetTeam(email, requestDTO.getTeamId())).thenReturn(team);
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        //when
        carService.updateCar(id, email, requestDTO);

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
        Long id = 1L;
        String email = "test@test.com";
        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(1L,
                "222나2222",
                "모델2",
                "색상2",
                2L,
                2,
                null);

        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MEMBER);

        //when & then
        assertThrows(RuntimeException.class,
                () -> carService.updateCar(id, email, requestDTO));

        verifyNoMoreInteractions(teamService);
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("차량 수정 - 실패(연료상태 등 범위 오류)")
    void carUpdate_fail_invalid_number() {
        //given
        Long id = 1L;
        String email = "test@test.com";
        UpdateCarRequestDTO requestDTO = new UpdateCarRequestDTO(1L,
                "222나2222",
                "모델2",
                "색상2",
                -1L,
                110,
                null);

        when(teamService.getUserRoleOfTeam(email, requestDTO.getTeamId())).thenReturn(Role.MEMBER);

        //when & then
        assertThrows(RuntimeException.class,
                () -> carService.updateCar(id, email, requestDTO));

        verifyNoMoreInteractions(teamService);
        verifyNoMoreInteractions(carRepository);
    }
}