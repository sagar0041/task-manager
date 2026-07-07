package dev.sagarpatil.taskmanager.dto;

public record CreateProjectRequest(
        String name,
        String description
) {}