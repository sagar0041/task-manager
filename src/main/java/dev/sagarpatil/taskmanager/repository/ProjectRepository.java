package dev.sagarpatil.taskmanager.repository;

import dev.sagarpatil.taskmanager.entity.Project;
import dev.sagarpatil.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByCreatedBy(User createdBy);
}