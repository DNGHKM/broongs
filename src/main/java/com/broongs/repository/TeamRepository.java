package com.broongs.repository;

import com.broongs.entity.Team;
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
    List<Team> getUserTeamList(Long userId);

    @Query("SELECT ut.team FROM UserTeam ut WHERE ut.team.id = :teamId AND ut.user.email = :email AND ut.team.deleted = false")
    Optional<Team> findTeamByIdAndUserEmail(@Param("email") String email, @Param("teamId") Long teamId);
}
