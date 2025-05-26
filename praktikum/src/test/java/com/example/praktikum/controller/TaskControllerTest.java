package com.example.praktikum.controller;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.praktikum.dto.PostTaskRequest;
import com.example.praktikum.model.Task;
import com.example.praktikum.service.TasksDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private TasksDatabase tasksDatabase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTask() throws Exception {
        PostTaskRequest request = new PostTaskRequest();
        request.setDescription("Testaufgabe");

        Task createdTask = new Task("123", "Testaufgabe", false);
        when(tasksDatabase.addTask(eq("Testaufgabe"), eq(false))).thenReturn(createdTask);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.description").value("Testaufgabe"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    public void testGetTask() throws Exception {
        Task task = new Task("abc", "Lernaufgabe", false);
        when(tasksDatabase.getTask("abc")).thenReturn(task);

        mockMvc.perform(get("/tasks/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc"))
                .andExpect(jsonPath("$.description").value("Lernaufgabe"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    public void testGetTask_NotFound() throws Exception {
        when(tasksDatabase.getTask("not-found")).thenReturn(null);

        mockMvc.perform(get("/tasks/not-found"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task t1 = new Task("1", "A", false);
        Task t2 = new Task("2", "B", true);

        when(tasksDatabase.getAllTasks()).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testPatchTask() throws Exception {
        String taskId = UUID.randomUUID().toString();
        Task updatedTask = new Task(taskId, "Geändert", true);

        when(tasksDatabase.updateTask(taskId, "Geändert", true)).thenReturn(updatedTask);

        String requestBody = """
            {
              "description": "Geändert",
              "done": true
            }
        """;

        mockMvc.perform(patch("/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.description").value("Geändert"))
                .andExpect(jsonPath("$.done").value(true));
    }
}
