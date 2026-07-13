package com.taskmanager.dto;

import com.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating and updating tasks. We separate this from the entity so we
 * don't accidentally expose internal fields like id or createdAt in request bodies.
 */
@Data
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private Task.Priority priority;

    // The assignee can be null when creating - it means the task is unassigned
    private Long assigneeId;

    private LocalDate dueDate;
}
