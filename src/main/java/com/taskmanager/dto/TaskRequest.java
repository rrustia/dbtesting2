package com.taskmanager.dto;

import com.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO used when tasks are created or updated.
 * It stays separate from the entity so request bodies only carry editable fields.
 */
@Data
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private Task.Priority priority;

    // A null assignee id means the task remains unassigned.
    private Long assigneeId;

    private LocalDate dueDate;
}
