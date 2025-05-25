package com.example.praktikum.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.praktikum.model.Task;

@Service

public class TasksDatabase {

    private final Map<String, Task> tasks = new HashMap<>();

    public Task addTask(String name, boolean done) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, name, done);
        tasks.put(id, task);
        return task;
    }

    public Task getTask(String id) {
        return tasks.get(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task updateTask(String id, String taskDescription, Boolean done) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setDescription(taskDescription);
            task.setDone(done);
        }
        return task;
    }


    
}