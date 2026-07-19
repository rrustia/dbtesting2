package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Holds the business rules for task operations.
 * Validation of related project and user records happens here before data is written.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    /**
     * Creates the service with the repositories used for task operations.
     * Input: the task, project, and user repositories.
     * Output: a configured task service instance.
     */
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns every task stored in the system.
     * Input: no arguments.
     * Output: a list of task response objects.
     */
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Loads one task by id.
     * Input: the task identifier.
     * Output: a task response for the matching record.
     */
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return toResponse(task);
    }

    /**
     * Creates a task inside one project.
     * Input: a task request and the project id that owns the task.
     * Output: the saved task converted to a response object.
     */
    @Transactional
    public TaskResponse createTask(TaskRequest request, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Task.Priority.MEDIUM)
                .project(project)
                .dueDate(request.getDueDate())
                .build();

        // The assignee is optional and only resolved when an id is present.
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    /**
     * Updates an existing task with the values supplied in the request.
     * Input: the task id and a request containing the desired changes.
     * Output: the updated task converted to a response object.
     */
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        // A new assignee is loaded only when the request contains an id.
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    /**
        * Changes only the status field on a task.
        * Input: the task id and the new status value.
        * Output: the updated task converted to a response object.
     */
    @Transactional
    public TaskResponse updateStatus(Long id, Task.TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    /**
     * Deletes a task by id.
     * Input: the task identifier.
     * Output: no return value; the matching task is removed when present.
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Finds tasks that belong to one project.
     * Input: the project identifier.
     * Output: a list of task response objects.
     */
    public List<TaskResponse> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Finds tasks assigned to one user.
     * Input: the assignee identifier.
     * Output: a list of task response objects.
     */
    public List<TaskResponse> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Finds tasks whose due date has passed and whose status is not done.
     * Input: no arguments.
     * Output: a list of overdue task response objects.
     */
    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), Task.TaskStatus.DONE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Converts a task entity into an API response.
     * Input: the task entity to map.
     * Output: a populated task response object.
     */
    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
                .assigneeUsername(task.getAssignee() != null ? task.getAssignee().getUsername() : null)
                .projectId(task.getProject().getId())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
