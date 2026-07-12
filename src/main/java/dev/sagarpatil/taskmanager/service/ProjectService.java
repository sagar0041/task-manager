package dev.sagarpatil.taskmanager.service;

import dev.sagarpatil.taskmanager.dto.CreateProjectRequest;
import dev.sagarpatil.taskmanager.dto.ProjectResponse;
import dev.sagarpatil.taskmanager.entity.Project;
import dev.sagarpatil.taskmanager.entity.User;
import dev.sagarpatil.taskmanager.repository.ProjectRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('ADMIN')")
    public ProjectResponse createProject(CreateProjectRequest request, User user) {
        org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());

        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .createdBy(user)
                .build();

        Project savedProject = projectRepository.save(project);

        return toResponse(savedProject);
    }

    public List<ProjectResponse> getMyProjects(User user) {
        List<Project> projects = projectRepository.findByCreatedBy(user);

        return projects.stream()
                .map(this::toResponse)
                .toList();
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
}