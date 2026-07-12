package dev.sagarpatil.taskmanager.controller;

import dev.sagarpatil.taskmanager.dto.CreateTaskRequest;
import dev.sagarpatil.taskmanager.dto.TaskResponse;
import dev.sagarpatil.taskmanager.entity.TaskStatus;
import dev.sagarpatil.taskmanager.entity.User;
import dev.sagarpatil.taskmanager.service.TaskService;
import dev.sagarpatil.taskmanager.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        return taskService.createTask(request, user);
    }

    @GetMapping
    public List<TaskResponse> getTasksByProject(@RequestParam String projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/my")
    public List<TaskResponse> getMyTasks(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        return taskService.getMyTasks(user);
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable String id,
            @RequestBody TaskStatus newStatus,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        return taskService.updateTaskStatus(id, newStatus, user);
    }
}