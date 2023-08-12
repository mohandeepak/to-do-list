package com.practise.todo.todo.service;

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

    public Task createTask(Task task) {
        Task createdTask = new Task();
        createdTask.setName(task.getName());
        createdTask.setDescription(task.getDescription());

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

    public void updateTaskById(Long id, Task task) {

        if (taskRepository.existsById(id)){
            task.setId(id);
            taskRepository.save(task);
        } else {
            throw new EntityNotFoundException("Task with ID " + id + " not found");
        }
    }
}
