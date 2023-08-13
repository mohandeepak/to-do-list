package com.practise.todo.todo.controller;

import com.practise.todo.todo.dto.CreateTaskDTO;
import com.practise.todo.todo.dto.TaskDTO;
import com.practise.todo.todo.dto.UpdateTaskDTO;
import com.practise.todo.todo.mapper.TaskMapper;
import com.practise.todo.todo.model.Task;
import com.practise.todo.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    private ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO){
        Task createdTask = taskService.createTask(createTaskDTO);
        TaskDTO taskDTO = taskMapper.mapToDTO(createdTask);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    @GetMapping
    private ResponseEntity<List<TaskDTO>> getAllTasks(){
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> taskDTOS = tasks.stream().map(taskMapper::mapToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
        Optional<Task> task = taskService.getTaskById(id);
        if(task.isPresent()) {
            return new ResponseEntity<>(taskMapper.mapToDTO(task.get()), HttpStatus.OK);
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
    private ResponseEntity<TaskDTO> updateTaskById(@PathVariable Long id, @RequestBody UpdateTaskDTO updateTaskDTO){
        Task updatedTask = taskService.updateTaskById(id, updateTaskDTO);
        TaskDTO taskDTO = taskMapper.mapToDTO(updatedTask);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

}
