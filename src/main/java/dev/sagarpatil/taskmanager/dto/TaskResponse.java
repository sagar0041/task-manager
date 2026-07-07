package dev.sagarpatil.taskmanager.dto;

import dev.sagarpatil.taskmanager.entity.TaskPriority;
import dev.sagarpatil.taskmanager.entity.TaskStatus;

import java.time.Instant;

public record TaskResponse(
        String id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        String projectId,
        String projectName,
        String assignedToName,
        String createdByName,
        Instant createdAt,
        Instant updatedAt
) {}