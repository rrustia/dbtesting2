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
 * Seeds the database with sample data on first run so the application has usable records.
 * When users already exist, the initializer exits without adding anything.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

        /**
         * Creates the data initializer with the repositories and encoder it needs.
         * Input: user, project, and task repositories plus the password encoder.
         * Output: a configured initializer instance.
         */
    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository,
                           TaskRepository taskRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

        /**
         * Adds demo users, projects, and tasks when the database is empty.
         * Input: startup arguments supplied by Spring Boot.
         * Output: no direct return value; sample records are saved when seeding is needed.
         */
    @Override
    public void run(String... args) {
                // Seeding only runs when the user table is empty.
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding database with sample data...");

        // A small set of demo accounts keeps local testing straightforward.
        User user1 = User.builder()
                .username("rrustia")
                .email("rrustia@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.USER)
                .build();

        User user2 = User.builder()
                .username("mike")
                .email("mike@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.USER)
                .build();

        User user3 = User.builder()
                .username("mary")
                .email("mary@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.USER)
                .build();

        userRepository.saveAll(java.util.List.of(user1, user2, user3));

        // Demo projects give the seeded users immediate data to browse.
        Project project1 = Project.builder()
                .name("Task Manager API")
                .description("Building the backend for the task management application")
                .owner(user1)
                .build();

        Project project2 = Project.builder()
                .name("Website Redesign")
                .description("Complete overhaul of the company website")
                .owner(user1)
                .build();

        projectRepository.saveAll(java.util.List.of(project1, project2));

        // Sample tasks fill the first project with realistic workflow data.
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
        System.out.println("Demo login credentials: rrustia/password123, mike/password123, or mary/password123");
    }
}
