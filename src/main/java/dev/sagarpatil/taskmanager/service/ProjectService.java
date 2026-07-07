package dev.sagarpatil.taskmanager.service;

import dev.sagarpatil.taskmanager.dto.CreateProjectRequest;
import dev.sagarpatil.taskmanager.dto.ProjectResponse;
import dev.sagarpatil.taskmanager.entity.Project;
import dev.sagarpatil.taskmanager.entity.User;
import dev.sagarpatil.taskmanager.repository.ProjectRepository;
import dev.sagarpatil.taskmanager.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(CreateProjectRequest request, String keycloakId) {
        // 1. find the User by keycloakId
        // 2. build a Project entity using Project.builder()... (name, description, createdBy)
        // 3. save it via projectRepository
        // 4. convert the saved Project to a ProjectResponse and return it

        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .createdBy(
                        user
                )
                .build();

        Project savedProject = projectRepository.save(project);

        return toResponse(savedProject);

    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedBy().getUsername(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    public List<ProjectResponse> getMyProjects(String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByCreatedBy(user);

        return projects.stream()
                .map(this::toResponse)
                .toList();
    }
}