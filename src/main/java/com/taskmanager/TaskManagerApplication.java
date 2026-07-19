package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {

    /**
     * Starts the Spring Boot application.
     * Input: command-line arguments passed at startup.
     * Output: no direct return value; the application context starts running.
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }
}
