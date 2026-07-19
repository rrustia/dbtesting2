package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles task-related REST endpoints for a project.
 * Request parsing stays here, while the service layer performs the actual task work.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Builds the controller with the task service dependency.
     * Input: the service that manages task operations.
     * Output: a configured controller instance.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Returns every task linked to one project.
     * Input: the project id from the request path.
     * Output: an HTTP response with a list of task records.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        List<TaskResponse> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Fetches one task by its identifier.
     * Input: the task id from the request path.
     * Output: an HTTP response with the matching task details.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId) {
        TaskResponse task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    /**
     * Creates a new task inside the selected project.
     * Input: the project id from the path and a validated task payload.
     * Output: an HTTP response with the created task and status 201.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse task = taskService.createTask(request, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    /**
         * Updates an existing task with the fields present in the request.
         * Input: the task id and a validated payload containing the new values.
         * Output: an HTTP response with the updated task data.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse task = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(task);
    }

    /**
         * Changes only the status field of a task.
         * Input: the task id and the new status from the request path.
         * Output: an HTTP response with the task after the status update.
     */
    @PatchMapping("/{taskId}/status/{status}")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long taskId,
            @PathVariable Task.TaskStatus status) {
        TaskResponse task = taskService.updateStatus(taskId, status);
        return ResponseEntity.ok(task);
    }

    /**
     * Deletes a task record.
     * Input: the task id from the request path.
     * Output: an empty HTTP 204 response when deletion succeeds.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Finds tasks assigned to one user.
     * Input: the assignee id from the request path.
     * Output: an HTTP response with the matching task list.
     */
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignee(@PathVariable Long assigneeId) {
        List<TaskResponse> tasks = taskService.getTasksByAssignee(assigneeId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Lists overdue tasks that are not finished yet.
     * Input: no request body; the route uses the current date in the service layer.
     * Output: an HTTP response with overdue task records.
     */
    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {
        List<TaskResponse> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }
}
