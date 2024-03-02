package com.example.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TeamDTO;
import com.example.taskmanager.model.persistance.Team;
import com.example.taskmanager.repository.TeamRepository;

@Service
public class TeamService {

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
        return convertTeamToDTO(team);
    }

    public List<TeamDTO> getTeamsByUser(Long userId) {
        List<Team> teams = teamRepository.findByUser(userId);
        List<TeamDTO> teamDTOs = new ArrayList<>();
        for (Team team : teams) {
            teamDTOs.add(convertTeamToDTO(team));
        }
        return teamDTOs;
    }

    public TeamDTO createTeam(TeamDTO teamDTO) throws CustomException {
        if (teamDTO.getName() == null || teamDTO.getName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team name cannot be empty");
        }
        Team newTeam = new Team();
        newTeam.setName(teamDTO.getName());
        Team createdTeam = teamRepository.save(newTeam);
        return convertTeamToDTO(createdTeam);
    }

    public TeamDTO updateTeam(TeamDTO teamDTO) throws CustomException {
        Team team = getTeamById(teamDTO.getId());
        if (team.getLastUpdateDate().isAfter(teamDTO.getLastUpdateDate())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team data are out-dated");
        }
        if (teamDTO.getName() == null || teamDTO.getName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Team name cannot be empty");
        }
        team.setName(teamDTO.getName());
        Team updatedTeam = teamRepository.save(team);
        return convertTeamToDTO(updatedTeam);
    }

    public void deleteTeam(Long id) throws CustomException {
        Team team = getTeamById(id);
        teamRepository.deleteById(team.getId());
    }

    private TeamDTO convertTeamToDTO(Team team) {
        return TeamDTO.fromObject(team);
    }
}
