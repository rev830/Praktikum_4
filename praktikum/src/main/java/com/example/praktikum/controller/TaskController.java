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

import jakarta.validation.Valid;


@RestController // Ist wie @Controller, aber gibt automatisch JSON zurück; Spart ein zusätzliches @ResponseBody bei jeder Methode
@RequestMapping("/tasks")

public class TaskController {
    
    private final TasksDatabase tasksDatabase;

    @Autowired // Da es nur einen Konstruktor gibt und dieser Konstruktor public ist -> @Autowired optional 
    public TaskController(TasksDatabase tasksDatabase) {
        this.tasksDatabase = tasksDatabase;
    }

    @GetMapping("/{id}") // GET
    public ResponseEntity<Task> getTask(@PathVariable String id) {
        Task task = tasksDatabase.getTask(id);
        if (task == null) {
            return ResponseEntity.notFound().build(); // Error 404 wenn Task nicht vorhanden
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasksDatabase.getAllTasks();
    }

    @PostMapping // POST
    public Task postTask(@Valid @RequestBody PostTaskRequest request) { //@Valid löst automatisch einen 400 Bad Request aus, wenn die Eingaben ungültig sind – kein manuelles Prüfen nötig
        return tasksDatabase.addTask(request.getDescription(), false);
    }

    @PatchMapping("/{id}") // PATCH
    public ResponseEntity<Task> patchTask(@PathVariable String id, @RequestBody PatchTaskRequest request) {
        Task task = tasksDatabase.updateTask(id, request.getDescription(), request.getDone());
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }
}
