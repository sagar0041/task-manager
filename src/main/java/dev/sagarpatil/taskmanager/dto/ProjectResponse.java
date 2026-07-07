package dev.sagarpatil.taskmanager.dto;

import java.time.Instant;

public record ProjectResponse(
        String id,
        String name,
        String description,
        String createdByName,
        Instant createdAt,
        Instant updatedAt
) {}