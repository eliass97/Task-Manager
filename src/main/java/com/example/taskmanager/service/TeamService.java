package com.example.taskmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TeamDTO;
import com.example.taskmanager.model.persistance.Team;
import com.example.taskmanager.repository.TeamRepository;

@Service
public class TeamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamById(Long teamId) throws CustomException {
        Optional<Team> team = teamRepository.findById(teamId);
        return team.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Team not found"));
    }

    public TeamDTO getTeamByName(String name) throws CustomException {
        Team team = teamRepository.findByName(name)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Team not found"));
        return TeamDTO.fromObject(team);
    }

    public List<TeamDTO> getTeamsByUser(Long userId) {
        List<Team> teams = teamRepository.findByUser(userId);
        List<TeamDTO> teamDTOs = new ArrayList<>();
        for (Team team : teams) {
            teamDTOs.add(TeamDTO.fromObject(team));
        }
        return teamDTOs;
    }

    public TeamDTO createTeam(TeamDTO teamDTO) throws CustomException {
        if (teamDTO.getName() == null || teamDTO.getName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team name cannot be empty");
        }

        LOGGER.info("Creating team with name: {}", teamDTO.getName());
        Team newTeam = new Team();
        newTeam.setName(teamDTO.getName());
        Team createdTeam = teamRepository.save(newTeam);

        return TeamDTO.fromObject(createdTeam);
    }

    public TeamDTO updateTeam(TeamDTO teamDTO) throws CustomException {
        Team team = getTeamById(teamDTO.getId());
        LocalDateTime lastUpdateDate = teamDTO.getLastUpdateDate();

        if (lastUpdateDate == null || team.getLastUpdateDate().isAfter(teamDTO.getLastUpdateDate())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team data are out-dated");
        }
        if (teamDTO.getName() == null || teamDTO.getName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team name cannot be empty");
        }

        LOGGER.info("Updating team with id: {}", team.getId());
        team.setName(teamDTO.getName());
        Team updatedTeam = teamRepository.save(team);

        return TeamDTO.fromObject(updatedTeam);
    }

    public void deleteTeam(Long id) throws CustomException {
        Team team = getTeamById(id);
        LOGGER.info("Deleting team with id: {}", team.getId());
        teamRepository.deleteById(team.getId());
    }
}
