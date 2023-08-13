package com.practise.todo.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practise.todo.todo.controller.TaskController;
import com.practise.todo.todo.dto.CreateTaskDTO;
import com.practise.todo.todo.dto.TaskDTO;
import com.practise.todo.todo.dto.UpdateTaskDTO;
import com.practise.todo.todo.mapper.TaskMapper;
import com.practise.todo.todo.model.Task;
import com.practise.todo.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private TaskMapper taskMapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllTasks() throws Exception{
		List<Task> tasks = new ArrayList<>();
		Task task1 = new Task();
		task1.setId(1L);
		task1.setName("task1");
		task1.setDescription("this is task1");
		tasks.add(task1);
		Task task2 = new Task();
		task2.setName("task2");
		task2.setDescription("this is task2");
		task2.setId(2L);
		tasks.add(task2);


		when(taskService.getAllTasks()).thenReturn(tasks);

		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setId(task1.getId());
		taskDTO1.setName(task1.getName());
		taskDTO1.setDescription(task1.getDescription());

		TaskDTO taskDTO2 = new TaskDTO();
		taskDTO2.setId(task2.getId());
		taskDTO2.setName(task2.getName());
		taskDTO2.setDescription(task2.getDescription());

		when(taskMapper.mapToDTO(task1)).thenReturn(taskDTO1);
		when(taskMapper.mapToDTO(task2)).thenReturn(taskDTO2);

		mockMvc.perform(get("/tasks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is("task1")))
				.andExpect(jsonPath("$[1].name", is("task2")));

	}

	@Test
	public void testGetTaskById() throws Exception{

		Task task1 = new Task();
		task1.setId(4L);
		task1.setName("task4");
		task1.setDescription("this is task4");


		when(taskService.getTaskById(4L)).thenReturn(Optional.of(task1));

		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setId(task1.getId());
		taskDTO1.setName(task1.getName());
		taskDTO1.setDescription(task1.getDescription());

		when(taskMapper.mapToDTO(task1)).thenReturn(taskDTO1);


		mockMvc.perform(get("/tasks/{id}", 4L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("task4")))
				.andExpect(jsonPath("$.description", is("this is task4")));

	}

	@Test
	public void testCreateTask() throws Exception{

		Task task1 = new Task();
		task1.setId(1L);
		task1.setName("task1");
		task1.setDescription("this is task1");


		CreateTaskDTO taskDTO1 = new CreateTaskDTO();
		taskDTO1.setName(task1.getName());
		taskDTO1.setDescription(task1.getDescription());

		when(taskService.createTask(taskDTO1)).thenReturn(task1);

		TaskDTO resultDTO = new TaskDTO();
		resultDTO.setId(task1.getId());
		resultDTO.setName(task1.getName());
		resultDTO.setDescription(task1.getDescription());

		when(taskMapper.mapToDTO(task1)).thenReturn(resultDTO);


		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDTO1)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("task1")))
				.andExpect(jsonPath("$.description", is("this is task1")));

	}

	@Test
	public void testDeleteTask() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setName("task1");
		task.setDescription("this is task1");
		when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
		mockMvc.perform(delete("/tasks/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(taskService, times(1)).deleteTaskById(1L);
	}

	@Test
	public void testUpdateTaskById() throws Exception {

		Task existingTask = new Task();
		existingTask.setId(1L);
		existingTask.setName("Original Name");
		existingTask.setDescription("Original Description");

		UpdateTaskDTO updateDTO = new UpdateTaskDTO();
		updateDTO.setName("Updated Name");
		updateDTO.setDescription("Updated Description");

		Task updatedTask = new Task();
		updatedTask.setId(existingTask.getId());
		updatedTask.setName(updateDTO.getName());
		updatedTask.setDescription(updateDTO.getDescription());
		when(taskService.updateTaskById(eq(1L), ArgumentMatchers.any(UpdateTaskDTO.class))).thenReturn(updatedTask);

		TaskDTO resultDTO = new TaskDTO();
		resultDTO.setId(updatedTask.getId());
		resultDTO.setName(updatedTask.getName());
		resultDTO.setDescription(updatedTask.getDescription());

		when(taskMapper.mapToDTO(updatedTask)).thenReturn(resultDTO);

		mockMvc.perform(put("/tasks/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Updated Name")))
				.andExpect(jsonPath("$.description", is("Updated Description")));

		verify(taskService, times(1)).updateTaskById(eq(1L), ArgumentMatchers.any(UpdateTaskDTO.class));
	}

}
