package dev.sagarpatil.taskmanager.controller;

import dev.sagarpatil.taskmanager.dto.CreateTaskRequest;
import dev.sagarpatil.taskmanager.dto.TaskResponse;
import dev.sagarpatil.taskmanager.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String keycloakId = jwt.getSubject();
        return taskService.createTask(request, keycloakId);
    }

    @GetMapping
    public List<TaskResponse> getTasksByProject(@RequestParam String projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/my")
    public List<TaskResponse> getMyTasks(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return taskService.getMyTasks(keycloakId);
    }
}