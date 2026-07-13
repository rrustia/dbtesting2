package com.taskmanager.dto;

import com.taskmanager.model.Task;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for tasks. This is what the frontend gets back - we include
 * assignee info as plain strings rather than nested objects to keep things simple.
 */
@Data
@Builder
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Task.TaskStatus status;
    private Task.Priority priority;
    private Long assigneeId;
    private String assigneeUsername;
    private Long projectId;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
