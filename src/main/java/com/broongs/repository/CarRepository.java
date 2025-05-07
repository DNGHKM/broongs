package com.broongs.repository;

import com.broongs.entity.Car;
import com.broongs.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findCarByNumberAndTeamId(String number, Long teamId);

    @Query("""
            SELECT c
            FROM Car c
            WHERE c.team = :team
            and c.deleted = false
            """)
    List<Car> findCarsByTeam(Team team);

    Optional<Car> findByIdAndDeletedFalse(Long id);
}
