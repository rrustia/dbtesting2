package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an application user who can own projects and receive task assignments.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // The role field keeps the access level attached to the user.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // The ownedProjects collection stores every project created by the user.
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Project> ownedProjects = new ArrayList<>();

    // The assignedTasks collection stores tasks currently assigned to the user.
    @OneToMany(mappedBy = "assignee")
    @Builder.Default
    private List<Task> assignedTasks = new ArrayList<>();

    public enum Role {
        ADMIN, USER
    }

    /**
     * Sets the creation timestamp before the user is inserted.
     * Input: no explicit arguments; JPA calls it during persistence.
     * Output: no return value; createdAt receives the current time.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
