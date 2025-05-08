package com.broongs.entity;

import com.broongs.dto.car.AddCarRequestDTO;
import com.broongs.dto.car.UpdateCarRequestDTO;
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

    @Column(name = "img_uuid", length = 50)
    private String imageUUID;

    @Column(nullable = false)
    private boolean available = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

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
                .deleted(false)
                .updateAt(LocalDateTime.now())
                .build();
    }

    public void update(UpdateCarRequestDTO dto, String imageUUID) {
        this.number = dto.getNumber();
        this.model = dto.getModel();
        this.color = dto.getColor();
        this.mileage = dto.getMileage();
        this.fuelLevel = dto.getFuelLevel();
        this.imageUUID = imageUUID;
        this.updateAt = LocalDateTime.now();
    }

    public void updateAvailable(boolean isAvailable) {
        this.available = isAvailable;
        this.updateAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
    }
}
