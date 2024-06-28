package com.example.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.persistance.TeamMemberRelationship;

@Repository
public interface TeamMemberRelationshipRepository extends JpaRepository<TeamMemberRelationship, Long> {

    Optional<TeamMemberRelationship> findByTeamIdAndMemberId(Long teamId, Integer memberId);
}
