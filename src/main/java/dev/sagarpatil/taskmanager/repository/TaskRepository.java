package dev.sagarpatil.taskmanager.repository;

import dev.sagarpatil.taskmanager.entity.Project;
import dev.sagarpatil.taskmanager.entity.Task;
import dev.sagarpatil.taskmanager.entity.TaskStatus;
import dev.sagarpatil.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignedTo(User assignedTo);
    List<Task> findByProjectAndStatus(Project project, TaskStatus status);
}