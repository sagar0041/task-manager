package dev.sagarpatil.taskmanager.service;

import dev.sagarpatil.taskmanager.dto.CreateTaskRequest;
import dev.sagarpatil.taskmanager.dto.TaskResponse;
import dev.sagarpatil.taskmanager.entity.Project;
import dev.sagarpatil.taskmanager.entity.Task;
import dev.sagarpatil.taskmanager.entity.TaskStatus;
import dev.sagarpatil.taskmanager.entity.User;
import dev.sagarpatil.taskmanager.repository.ProjectRepository;
import dev.sagarpatil.taskmanager.repository.TaskRepository;
import dev.sagarpatil.taskmanager.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public TaskResponse createTask(CreateTaskRequest request, User createdBy) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignedTo = null;
        if (request.assignedTo() != null) {
            assignedTo = userRepository.findById(request.assignedTo())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(TaskStatus.BACKLOG)
                .project(project)
                .assignedTo(assignedTo)
                .createdBy(createdBy)
                .build();

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    public List<TaskResponse> getTasksByProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return taskRepository.findByProject(project).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getMyTasks(User user) {
        return taskRepository.findByAssignedTo(user).stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null,
                task.getCreatedBy().getUsername(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    @PreAuthorize("isAuthenticated()")
    public TaskResponse updateTaskStatus(String taskId, TaskStatus newStatus, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isManager = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROJECT_MANAGER")
                        || a.getAuthority().equals("ROLE_ADMIN"));

        boolean isAssignee = task.getAssignedTo() != null
                && task.getAssignedTo().getId().equals(currentUser.getId());

        if (!isManager && !isAssignee) {
            throw new RuntimeException("Not authorized to update this task");
        }

        task.setStatus(newStatus);
        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }
}