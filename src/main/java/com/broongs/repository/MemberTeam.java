package com.broongs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTeam extends JpaRepository<MemberTeam, Long> {
}
