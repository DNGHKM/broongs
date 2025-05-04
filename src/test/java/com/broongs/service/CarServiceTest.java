package com.broongs.service;

import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.AddCarResponseDTO;
import com.broongs.entity.Car;
import com.broongs.entity.Team;
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
        when(carRepository.findCarByNumber(requestDTO.getNumber())).thenReturn(Optional.ofNullable(duplicatedCar));

        // when/then
        assertThrows(RuntimeException.class, () -> {
            carService.addCar(email, requestDTO);
        });
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    @DisplayName("차량등록 - 실패(팀이 없거나 권한 없음)")
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

        when(teamService.validateUserHasAccessAndGetTeam(email, requestDTO.getTeamId())).thenReturn(null);

        // when/then
        assertThrows(RuntimeException.class, () -> {
            carService.addCar(email, requestDTO);
        });
    }
}