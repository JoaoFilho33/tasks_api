package com.projeto.tasks_api.controller;

import com.projeto.tasks_api.models.Task;
import com.projeto.tasks_api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getTasks(@RequestParam(value = "order_by", required = false) String orderBy) {
        List<Task> tasks = taskRepository.findAll();

        tasks.sort(Comparator
                .comparing(Task::isDone, Comparator.reverseOrder())
                .thenComparing(Task::getName)
                .thenComparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
        );

        return tasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok().body(task))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTag(@RequestBody Task task) {
        // Log a mensagem com os detalhes da solicitação recebida
        System.out.println("Received request: " + task.toString());
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok().body(savedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
        return ResponseEntity.notFound().build();
    }
}
