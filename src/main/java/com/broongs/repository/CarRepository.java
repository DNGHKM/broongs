package com.broongs.repository;

import com.broongs.entity.Car;
import com.broongs.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Location> {
}
