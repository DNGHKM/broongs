package com.broongs.entity;

import com.broongs.dto.parking.ParkingRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Park {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(length = 255)
    private String comment;

    @Embedded
    private Location location;

    private LocalDateTime time;

    @Column(name = "img_uuid", length = 50)
    private String imageUUID;

    protected Park() {
    }

    public static Park parking(User user, Car car, ParkingRequestDTO dto, String imageUUID) {
        return Park.builder()
                .user(user)
                .car(car)
                .comment(dto.getComment())
                .location(dto.getLocation())
                .time(dto.getTime())
                .imageUUID(imageUUID)
                .build();
    }
}
