package com.example.taskmanager.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamMemberRelationshipService.class);

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

    @Transactional(rollbackOn = CustomException.class)
    public void createRelationship(TeamMemberRelationshipDTO teamMemberRelationshipDTO) throws CustomException {
        Integer userId = teamMemberRelationshipDTO.getUserId();
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

        LOGGER.info("Creating team member relationship for team id: {} and user id: {}", team.getId(), member.getId());
        TeamMemberRelationship newTeamMemberRelationship = new TeamMemberRelationship();
        newTeamMemberRelationship.setTeam(team);
        newTeamMemberRelationship.setMember(member);
        newTeamMemberRelationship.setRole(role);

        teamMemberRelationshipRepository.save(newTeamMemberRelationship);
    }

    public void deleteRelationship(TeamMemberRelationshipDTO teamMemberRelationshipDTO) throws CustomException {
        Integer userId = teamMemberRelationshipDTO.getUserId();
        if (userId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "User id must be provided");
        }
        Long teamId = teamMemberRelationshipDTO.getTeamId();
        if (teamId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team id must be provided");
        }

        TeamMemberRelationship teamMemberRelationship = teamMemberRelationshipRepository.findByTeamIdAndMemberId(teamId, userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Relationship not found"));

        LOGGER.info("Deleting team member relationship with id: {}", teamMemberRelationship.getId());
        teamMemberRelationshipRepository.delete(teamMemberRelationship);
    }
}
