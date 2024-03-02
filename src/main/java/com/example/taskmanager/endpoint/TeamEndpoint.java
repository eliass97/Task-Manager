package com.example.taskmanager.endpoint;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TeamDTO;
import com.example.taskmanager.service.TeamService;

@RestController
@RequestMapping("api/team")
public class TeamEndpoint {

    private final TeamService teamService;

    public TeamEndpoint(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public TeamDTO getTeamByName(@RequestParam("name") String name) throws CustomException {
        return teamService.getTeamByName(name);
    }

    @GetMapping("/user/{userId}")
    public List<TeamDTO> getTeamsByUser(@PathVariable("userId") Long userId) {
        return teamService.getTeamsByUser(userId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_TEAM')")
    public TeamDTO createTeam(@RequestBody TeamDTO teamDTO) throws CustomException {
        return teamService.createTeam(teamDTO);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('CREATE_TEAM')")
    public TeamDTO updateTeam(@RequestBody TeamDTO teamDTO) throws CustomException {
        return teamService.updateTeam(teamDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('DELETE_TEAM')")
    public ResponseEntity<Object> deleteTeam(@RequestParam("id") Long id) throws CustomException {
        teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
