package dev.sagarpatil.taskmanager.dto;

import dev.sagarpatil.taskmanager.entity.TaskPriority;

public record CreateTaskRequest(
        String title,
        String description,
        TaskPriority priority,
        String projectId,
        String assignedTo
) {}