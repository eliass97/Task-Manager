package com.example.taskmanager.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TeamMemberRelationshipDTO;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.Team;
import com.example.taskmanager.model.persistance.TeamMemberRelationship;
import com.example.taskmanager.repository.TeamMemberRelationshipRepository;

@Service
public class TeamMemberRelationshipService {

    private final TeamMemberRelationshipRepository teamMemberRelationshipRepository;
    private final TeamService teamService;
    private final SystemUserService systemUserService;

    public TeamMemberRelationshipService(
            TeamMemberRelationshipRepository teamMemberRelationshipRepository,
            TeamService teamService,
            SystemUserService systemUserService
    ) {
        this.teamMemberRelationshipRepository = teamMemberRelationshipRepository;
        this.teamService = teamService;
        this.systemUserService = systemUserService;
    }

    @Transactional
    public void createRelationship(TeamMemberRelationshipDTO teamMemberRelationshipDTO) throws CustomException {
        Long userId = teamMemberRelationshipDTO.getUserId();
        if (userId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User id must be provided");
        }
        Long teamId = teamMemberRelationshipDTO.getTeamId();
        if (teamId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team id must be provided");
        }
        String role = teamMemberRelationshipDTO.getRole();
        if (role == null || role.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Role must be provided");
        }

        Optional<TeamMemberRelationship> teamMemberRelationship = teamMemberRelationshipRepository.findByTeamIdAndMemberId(teamId, userId);
        if (teamMemberRelationship.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "Relationship already exists");
        }

        Team team = teamService.getTeamById(teamId);
        SystemUser member = systemUserService.getUserById(userId);

        TeamMemberRelationship newTeamMemberRelationship = new TeamMemberRelationship();
        newTeamMemberRelationship.setTeam(team);
        newTeamMemberRelationship.setMember(member);
        newTeamMemberRelationship.setRole(role);

        teamMemberRelationshipRepository.save(newTeamMemberRelationship);
    }

    public void deleteRelationship(TeamMemberRelationshipDTO teamMemberRelationshipDTO) throws CustomException {
        Long userId = teamMemberRelationshipDTO.getUserId();
        if (userId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User id must be provided");
        }
        Long teamId = teamMemberRelationshipDTO.getTeamId();
        if (teamId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team id must be provided");
        }

        Optional<TeamMemberRelationship> teamMemberRelationship = teamMemberRelationshipRepository.findByTeamIdAndMemberId(teamId, userId);
        if (teamMemberRelationship.isEmpty()) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Relationship not found");
        }

        teamMemberRelationshipRepository.delete(teamMemberRelationship.get());
    }
}
