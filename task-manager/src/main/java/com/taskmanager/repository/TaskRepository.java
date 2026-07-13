package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByStatus(Task.TaskStatus status);

    List<Task> findByPriority(Task.Priority priority);

    // Find tasks that are past their due date - useful for a "overdue" view
    List<Task> findByDueDateBeforeAndStatusNot(LocalDate date, Task.TaskStatus status);

    long countByProjectId(Long projectId);
}
