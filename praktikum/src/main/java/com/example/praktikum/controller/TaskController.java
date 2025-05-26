package com.example.praktikum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.praktikum.dto.PatchTaskRequest;
import com.example.praktikum.dto.PostTaskRequest;
import com.example.praktikum.model.Task;
import com.example.praktikum.service.TasksDatabase;


@RestController
@RequestMapping("/tasks")

public class TaskController {
    
    private final TasksDatabase tasksDatabase;

    @Autowired
    public TaskController(TasksDatabase tasksDatabase) {
        this.tasksDatabase = tasksDatabase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        Task task = tasksDatabase.getTask(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasksDatabase.getAllTasks();
    }

    @PostMapping
    public Task postTask(@RequestBody PostTaskRequest request) {
        return tasksDatabase.addTask(request.getDescription(), false);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> patchTask(@PathVariable String id, @RequestBody PatchTaskRequest request) {
        Task task = tasksDatabase.updateTask(id, request.getDescription(), request.getDone());
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }
}
