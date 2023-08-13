package com.practise.todo.todo.mapper;

import com.practise.todo.todo.dto.TaskDTO;
import com.practise.todo.todo.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDTO mapToDTO(Task task){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        return taskDTO;
    }
}
