package com.broongs.repository;

import com.broongs.entity.UserTeam;
import com.broongs.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    @Query("SELECT u.role from UserTeam u where u.team.id=:teamId and u.user.id =:userId")
    Role findUserRole(Long teamId, Long userId);
}
