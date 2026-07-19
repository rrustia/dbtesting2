package com.taskmanager.service;

import com.taskmanager.dto.ProjectRequest;
import com.taskmanager.dto.ProjectResponse;
import com.taskmanager.exception.ResourceNotFoundException;
import com.taskmanager.model.Project;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contains the business logic for creating, reading, updating, and deleting projects.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
    * Creates the service with the repositories it depends on.
    * Input: the project, task, and user repositories.
    * Output: a configured project service instance.
    */
    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Finds all projects owned by one user.
     * Input: the owner id used for filtering.
     * Output: a list of project response objects.
     */
    public List<ProjectResponse> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Loads one project by id.
     * Input: the project identifier.
     * Output: a project response for the matching record.
     */
    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return toResponse(project);
    }

    /**
     * Creates a new project for one owner.
     * Input: a project request and the owner id.
     * Output: the saved project converted to a response object.
     */
    public ProjectResponse createProject(ProjectRequest request, Long ownerId) {
        // Duplicate names are blocked within the same owner's project list.
        if (projectRepository.existsByNameAndOwnerId(request.getName(), ownerId)) {
            throw new IllegalArgumentException("A project with this name already exists");
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .build();

        return toResponse(projectRepository.save(project));
    }

    /**
     * Updates the editable fields on an existing project.
     * Input: the project id and a request carrying the new values.
     * Output: the updated project converted to a response object.
     */
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        if (request.getName() != null) {
            String newName = request.getName().trim();
            if (!newName.isEmpty() && !newName.equals(project.getName())) {
                boolean exists = projectRepository.existsByNameAndOwnerId(
                        newName, project.getOwner().getId());
                if (exists) {
                    throw new IllegalArgumentException("A project with this name already exists");
                }
            }
            project.setName(newName);
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        Project updated = projectRepository.save(project);
        return toResponse(updated);
    }

    /**
     * Deletes one project by id.
     * Input: the project identifier.
     * Output: no return value; the matching record is removed when it exists.
     */
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    /**
     * Converts a project entity into an API response.
     * Input: the project entity to map.
     * Output: a populated project response object.
     */
    private ProjectResponse toResponse(Project project) {
        long taskCount = taskRepository.countByProjectId(project.getId());
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner() != null ? project.getOwner().getId() : null)
                .ownerUsername(project.getOwner() != null ? project.getOwner().getUsername() : null)
                .taskCount((int) taskCount)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
