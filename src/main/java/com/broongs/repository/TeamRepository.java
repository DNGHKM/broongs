package com.broongs.repository;

import com.broongs.entity.Team;
import com.broongs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
