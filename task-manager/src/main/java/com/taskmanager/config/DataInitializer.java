package com.taskmanager.config;

import com.taskmanager.model.Project;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.ProjectRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the database with sample data on first run so you don't start with an empty app.
 * This only runs once - if users already exist, it skips everything.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository,
                           TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Only seed if there are no users yet
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding database with sample data...");

        // Create a default admin and a regular user for testing
        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(User.Role.ADMIN)
                .build();

        User user1 = User.builder()
                .username("johndoe")
                .email("john@example.com")
                .password(passwordEncoder.encode("pass123"))
                .role(User.Role.USER)
                .build();

        User user2 = User.builder()
                .username("janedoe")
                .email("jane@example.com")
                .password(passwordEncoder.encode("pass123"))
                .role(User.Role.USER)
                .build();

        userRepository.saveAll(java.util.List.of(admin, user1, user2));

        // Create a couple of sample projects
        Project project1 = Project.builder()
                .name("Task Manager API")
                .description("Building the backend for our task management app")
                .owner(user1)
                .build();

        Project project2 = Project.builder()
                .name("Website Redesign")
                .description("Complete overhaul of the company website")
                .owner(user1)
                .build();

        projectRepository.saveAll(java.util.List.of(project1, project2));

        // Add some tasks to the first project
        Task task1 = Task.builder()
                .title("Set up Spring Security with JWT")
                .description("Implement token-based authentication for all API endpoints")
                .status(Task.TaskStatus.DONE)
                .priority(Task.Priority.HIGH)
                .assignee(user1)
                .project(project1)
                .build();

        Task task2 = Task.builder()
                .title("Write REST controllers")
                .description("Create CRUD endpoints for tasks and projects")
                .status(Task.TaskStatus.DONE)
                .priority(Task.Priority.HIGH)
                .assignee(user2)
                .project(project1)
                .build();

        Task task3 = Task.builder()
                .title("Add API documentation")
                .description("Document all endpoints with example requests and responses")
                .status(Task.TaskStatus.IN_PROGRESS)
                .priority(Task.Priority.MEDIUM)
                .assignee(user1)
                .project(project1)
                .build();

        Task task4 = Task.builder()
                .title("Write integration tests")
                .description("Cover all controller endpoints with test cases")
                .status(Task.TaskStatus.TODO)
                .priority(Task.Priority.MEDIUM)
                .assignee(user2)
                .project(project1)
                .build();

        Task task5 = Task.builder()
                .title("Set up CI/CD pipeline")
                .description("Automate builds and deployments")
                .status(Task.TaskStatus.TODO)
                .priority(Task.Priority.LOW)
                .assignee(null)
                .project(project1)
                .build();

        taskRepository.saveAll(java.util.List.of(task1, task2, task3, task4, task5));

        System.out.println("Sample data seeded successfully.");
        System.out.println("You can login with admin/admin123 or johndoe/pass123");
    }
}
