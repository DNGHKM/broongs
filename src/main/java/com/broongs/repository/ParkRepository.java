package com.broongs.repository;

import com.broongs.entity.Car;
import com.broongs.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkRepository extends JpaRepository<Park, Long> {
    List<Park> findParksByCar(Car car);
}
