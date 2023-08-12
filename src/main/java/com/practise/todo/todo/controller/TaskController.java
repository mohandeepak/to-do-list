package com.practise.todo.todo.controller;

import com.practise.todo.todo.model.Task;
import com.practise.todo.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    private TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    private ResponseEntity<Task> createTask(@RequestBody Task task){
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    private ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Task> getTaskById(@PathVariable Long id){
        Optional<Task> task = taskService.getTaskById(id);
        if(task.isPresent()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteTaskById(@PathVariable Long id){
        Optional<Task> task = taskService.getTaskById(id);
        if(task.isPresent()){
            taskService.deleteTaskById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateTaskById(@PathVariable Long id, @RequestBody Task task){
        taskService.updateTaskById(id, task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
