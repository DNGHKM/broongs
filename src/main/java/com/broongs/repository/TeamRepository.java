package com.broongs.repository;

import com.broongs.entity.Team;
import com.broongs.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("""
            SELECT t
            from Team t left join UserTeam u
            on t.id = u.team.id
            where u.user.id = :userId
                and t.deleted = false
            order by t.name
            """)
    List<Team> getUserTeamList(@Param("userId") Long userId);

    @Query("""
            SELECT ut.team
            FROM UserTeam ut
            WHERE ut.team.id = :teamId
            AND ut.user.email = :email
            AND ut.team.deleted = false
            """)
    Optional<Team> findAccessibleTeam(@Param("email") String email, @Param("teamId") Long teamId);

    @Query("""
            SELECT ut.role
            FROM UserTeam ut
            WHERE ut.team.id = :teamId
            AND ut.user.email = :email
            AND ut.team.deleted = false
            """)
    Role findUserRoleOfTeam(@Param("email") String email, @Param("teamId") Long teamId);

    @Query("""
            SELECT COUNT(ut) > 0
            FROM UserTeam ut
            WHERE ut.user.email = :email
              AND ut.team.id = :teamId
              AND ut.team.deleted = false
            """)
    boolean existsByUserEmailAndTeamId(@Param("email") String email, @Param("teamId") Long teamId);
}
