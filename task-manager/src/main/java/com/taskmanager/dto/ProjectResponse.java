package com.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for projects. We include the task count so the client can show
 * "X tasks" on the project card without having to make a separate request.
 */
@Data
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerUsername;
    private int taskCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
