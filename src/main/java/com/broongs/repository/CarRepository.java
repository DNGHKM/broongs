package com.broongs.repository;

import com.broongs.entity.Car;
import com.broongs.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findCarByNumber(String number);

    List<Car> findCarsByTeam(Team team);
}
