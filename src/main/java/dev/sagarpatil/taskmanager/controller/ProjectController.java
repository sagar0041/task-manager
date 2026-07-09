package dev.sagarpatil.taskmanager.controller;

import dev.sagarpatil.taskmanager.dto.CreateProjectRequest;
import dev.sagarpatil.taskmanager.dto.ProjectResponse;
import dev.sagarpatil.taskmanager.service.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();
        return projectService.createProject(request, keycloakId);
    }

    @GetMapping
    public List<ProjectResponse> getMyProjects(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return projectService.getMyProjects(keycloakId);
    }
}