package com.example.taskmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    @Query("select t from Team t where t.id in (select tmr.team.id from TeamMemberRelationship tmr where tmr.member.id = :userId)")
    List<Team> findByUser(@Param("userId") Long userId);
}
