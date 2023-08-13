package com.practise.todo.todo.service;

import com.practise.todo.todo.dto.CreateTaskDTO;
import com.practise.todo.todo.dto.TaskDTO;
import com.practise.todo.todo.dto.UpdateTaskDTO;
import com.practise.todo.todo.model.Task;
import com.practise.todo.todo.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    private TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task createTask(CreateTaskDTO createTaskDTO) {
        Task createdTask = new Task();
        createdTask.setName(createTaskDTO.getName());
        createdTask.setDescription(createTaskDTO.getDescription());

       return taskRepository.saveAndFlush(createdTask);

    }

    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {

        return taskRepository.findById(id);
    }

    public void deleteTaskById(Long id) {

       taskRepository.deleteById(id);
    }

    public Task updateTaskById(Long id, UpdateTaskDTO updateTaskDTO) {

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found with id" + id));

        existingTask.setName(updateTaskDTO.getName());
        existingTask.setDescription(updateTaskDTO.getDescription());
        return taskRepository.save(existingTask);
    }
}
