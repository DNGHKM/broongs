package com.broongs.entity;

import com.broongs.dto.car.AddCarRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false, length = 20)
    private String color;

    private Long mileage;

    private Integer fuelLevel;

    @Column(name = "img_uuid", length = 36)
    private String imageUUID;

    @Column(nullable = false)
    private boolean available = true;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    protected Car() {
    }

    public static Car addCar(Team team, AddCarRequestDTO dto, String imageUUID) {
        return Car.builder()
                .team(team)
                .number(dto.getNumber())
                .model(dto.getModel())
                .color(dto.getColor())
                .mileage(dto.getMileage())
                .fuelLevel(dto.getFuelLevel())
                .imageUUID(imageUUID)
                .available(true)
                .updateAt(LocalDateTime.now())
                .build();
    }
}
