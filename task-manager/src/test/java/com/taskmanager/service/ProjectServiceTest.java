package com.taskmanager.service;

import com.taskmanager.dto.ProjectRequest;
import com.taskmanager.dto.ProjectResponse;
import com.taskmanager.model.Project;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void updateProjectWithSameNameShouldAllowDescriptionChange() {
        User owner = User.builder()
                .id(10L)
                .username("owner")
                .build();

        Project existingProject = Project.builder()
                .id(1L)
                .name("Alpha")
                .description("old description")
                .owner(owner)
                .build();

        ProjectRequest request = new ProjectRequest();
        request.setName("Alpha");
        request.setDescription("updated description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(taskRepository.countByProjectId(1L)).thenReturn(0L);

        ProjectResponse response = assertDoesNotThrow(() -> projectService.updateProject(1L, request));

        assertEquals("Alpha", response.getName());
        assertEquals("updated description", response.getDescription());
    }
}
