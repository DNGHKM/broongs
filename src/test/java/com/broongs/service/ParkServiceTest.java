package com.broongs.service;

import com.broongs.dto.parking.ParkingRequestDTO;
import com.broongs.dto.parking.ParkingResponseDTO;
import com.broongs.entity.Car;
import com.broongs.entity.Location;
import com.broongs.entity.Team;
import com.broongs.entity.User;
import com.broongs.repository.ParkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ParkServiceTest {
    @Mock
    private CarService carService;
    @Mock
    private UserService userService;
    @Mock
    private TeamService teamService;
    @Mock
    private ParkRepository parkRepository;
    @InjectMocks
    private ParkService parkService;

    @Test
    @DisplayName("주차 등록 - 성공")
    void parking_success() {
        Long carId = 200L;
        Location location = new Location(123.4566, 34.5678);
        ParkingRequestDTO requestDTO = new ParkingRequestDTO("1층에다 주차했음",
                location,
                LocalDateTime.now(),
                null);

        Team team = Team.builder()
                .id(5L)
                .name("팀1")
                .build();

        User user = User.builder()
                .id(100L)
                .email("test@test.com")
                .build();

        Car car = Car.builder()
                .id(carId)
                .team(team)
                .number("123가1234")
                .build();

        when(carService.findUndeletedCarById(car.getId())).thenReturn(car);
        when(userService.findUserByEmail(user.getEmail())).thenReturn(user);

        //when
        ParkingResponseDTO responseDTO = parkService.parking(carId, requestDTO, user.getEmail());

        //then
        assertEquals(responseDTO.getComment(), requestDTO.getComment());
        assertEquals(responseDTO.getCarId(), car.getId());
        assertEquals(responseDTO.getLocation().getLatitude(), location.getLatitude());
        assertEquals(responseDTO.getLocation().getLongitude(), location.getLongitude());
    }

    @Test
    @DisplayName("주차 등록 - 실패(권한 없음)")
    void parking_fail() {
        Long carId = 200L;
        ParkingRequestDTO requestDTO = new ParkingRequestDTO("1층에다 주차했음",
                new Location(123.4566, 34.5678),
                LocalDateTime.now(),
                null);

        Team team = Team.builder()
                .id(5L)
                .name("팀1")
                .build();

        User user = User.builder()
                .id(100L)
                .email("test@test.com")
                .build();

        Car car = Car.builder()
                .id(carId)
                .team(team)
                .number("123가1234")
                .build();

        when(carService.findUndeletedCarById(car.getId())).thenReturn(car);
        when(userService.findUserByEmail(user.getEmail())).thenReturn(user);
        doThrow(RuntimeException.class).when(teamService).validateAccessToTeam(user.getEmail(), car.getTeam().getId());

        //when & then
        assertThrows(RuntimeException.class, () -> parkService.parking(carId, requestDTO, user.getEmail()));

        verifyNoMoreInteractions(parkRepository);
    }
}