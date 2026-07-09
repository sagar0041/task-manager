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

    public TaskResponse createTask(CreateTaskRequest request, String keycloakId) {
        User createdBy = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    public List<TaskResponse> getMyTasks(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
}