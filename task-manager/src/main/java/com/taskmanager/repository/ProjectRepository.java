package com.taskmanager.repository;

import com.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long ownerId);

    // Check if a project with this name already exists for the given owner.
    boolean existsByNameAndOwnerId(String name, Long ownerId);
}
