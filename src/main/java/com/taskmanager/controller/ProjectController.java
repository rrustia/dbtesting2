package com.taskmanager.controller;

import com.taskmanager.dto.ProjectRequest;
import com.taskmanager.dto.ProjectResponse;
import com.taskmanager.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Serves project-related REST endpoints.
 * The controller stays focused on request handling and delegates the business rules to the service layer.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Creates the controller with the project service dependency.
     * Input: the service that handles project operations.
     * Output: a ready-to-use controller instance.
     */
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Looks up all projects for one owner.
     * Input: the owner id from the request path.
     * Output: an HTTP response with a list of project summaries.
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByOwner(@PathVariable Long ownerId) {
        List<ProjectResponse> projects = projectService.getProjectsByOwner(ownerId);
        return ResponseEntity.ok(projects);
    }

    /**
     * Fetches one project by its identifier.
     * Input: the project id from the request path.
     * Output: an HTTP response with the matching project details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        ProjectResponse project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * Creates a project for the selected owner.
     * Input: the owner id from the path and a validated project payload.
     * Output: an HTTP response with the created project and status 201.
     */
    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<ProjectResponse> createProject(
            @PathVariable Long ownerId,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse project = projectService.createProject(request, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    /**
         * Applies updates to a project's editable fields.
         * Input: the project id and a validated payload with the new values.
         * Output: an HTTP response with the updated project data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    /**
     * Removes a project and lets cascade rules clean up related tasks.
     * Input: the project id from the request path.
     * Output: an empty HTTP 204 response when deletion succeeds.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
