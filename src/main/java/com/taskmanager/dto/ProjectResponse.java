package com.taskmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for projects.
 * The task count is included so clients can show quick project totals without an extra request.
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
