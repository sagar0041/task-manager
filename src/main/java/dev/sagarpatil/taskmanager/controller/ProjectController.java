package dev.sagarpatil.taskmanager.controller;

import dev.sagarpatil.taskmanager.dto.CreateProjectRequest;
import dev.sagarpatil.taskmanager.dto.ProjectResponse;
import dev.sagarpatil.taskmanager.entity.User;
import dev.sagarpatil.taskmanager.service.ProjectService;
import dev.sagarpatil.taskmanager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        return projectService.createProject(request, user);
    }

    @GetMapping
    public List<ProjectResponse> getMyProjects(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        return projectService.getMyProjects(user);
    }
}