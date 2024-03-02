package com.example.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.TaskDTO;
import com.example.taskmanager.model.dto.TaskSearchCriteria;
import com.example.taskmanager.model.enums.TaskStatusName;
import com.example.taskmanager.model.persistance.QTask;
import com.example.taskmanager.model.persistance.SystemUser;
import com.example.taskmanager.model.persistance.Task;
import com.example.taskmanager.model.persistance.TaskStatus;
import com.example.taskmanager.model.persistance.UserProfile;
import com.example.taskmanager.repository.TaskRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final SystemUserService systemUserService;

    public TaskService(
            TaskRepository taskRepository,
            TaskStatusService taskStatusService,
            SystemUserService systemUserService
    ) {
        this.taskRepository = taskRepository;
        this.taskStatusService = taskStatusService;
        this.systemUserService = systemUserService;
    }

    public TaskDTO getTaskDTOById(Long id) throws CustomException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Task not found"));
        return convertTaskToDTO(task);
    }

    public Task getTaskById(Long id) throws CustomException {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public List<TaskDTO> getTasksBySearchCriteria(TaskSearchCriteria taskSearchCriteria) throws CustomException {
        List<Predicate> predicates = new ArrayList<>();
        if (taskSearchCriteria.getAssigneeId() != null) {
            Predicate predicate = QTask.task.assignee.id.eq(taskSearchCriteria.getAssigneeId());
            predicates.add(predicate);
        }
        if (taskSearchCriteria.getDescription() != null && !taskSearchCriteria.getDescription().isEmpty()) {
            Predicate predicate = QTask.task.description.like(taskSearchCriteria.getDescription());
            predicates.add(predicate);
        }
        if (taskSearchCriteria.getStatusName() != null && !taskSearchCriteria.getStatusName().isEmpty()) {
            Predicate predicate = QTask.task.taskStatus.name.eq(taskSearchCriteria.getStatusName());
            predicates.add(predicate);
        }

        Predicate predicate = ExpressionUtils.allOf(predicates);
        if (predicate == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Failed to create a predicate from the provided criteria");
        }

        List<TaskDTO> taskDTOs = new ArrayList<>();
        taskRepository.findAll(predicate).forEach(task -> taskDTOs.add(convertTaskToDTO(task)));
        return taskDTOs;
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) throws CustomException {
        doBasicValidationChecks(taskDTO);

        SystemUser assignee = systemUserService.getUserById(taskDTO.getAssigneeId());
        TaskStatus taskStatus = taskStatusService.getTaskStatusByName(TaskStatusName.NEW.getValue());

        Task newTask = new Task();
        populateTask(newTask, taskDTO, assignee, taskStatus);

        Task createdTask = taskRepository.save(newTask);
        return convertTaskToDTO(createdTask);
    }

    @Transactional
    public TaskDTO updateTask(TaskDTO taskDTO) throws CustomException {
        Task task = getTaskById(taskDTO.getId());
        if (task.getLastUpdateDate().isAfter(taskDTO.getLastUpdateDate())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Task data are out-dated");
        }

        doBasicValidationChecks(taskDTO);

        SystemUser assignee = systemUserService.getUserById(taskDTO.getAssigneeId());
        TaskStatus taskStatus = taskStatusService.getTaskStatusByName(taskDTO.getTaskStatus().getName());
        populateTask(task, taskDTO, assignee, taskStatus);

        Task updatedTask = taskRepository.save(task);
        return convertTaskToDTO(updatedTask);
    }

    public void deleteTask(Long id) throws CustomException {
        Task task = getTaskById(id);
        taskRepository.deleteById(task.getId());
    }

    private TaskDTO convertTaskToDTO(Task task) {
        SystemUser assignee = task.getAssignee();
        UserProfile userProfile = assignee.getProfile();
        String fullName = String.format("%s %s", userProfile.getFirstName(), userProfile.getLastName());
        return TaskDTO.fromObject(task, assignee.getId(), fullName);
    }

    private void doBasicValidationChecks(TaskDTO taskDTO) throws CustomException {
        if (taskDTO.getName() == null || taskDTO.getName().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Task name is empty");
        }
        if (taskDTO.getDescription() == null || taskDTO.getDescription().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Task description is empty");
        }
        if (taskDTO.getAssigneeId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Task assignee is empty");
        }
        if (taskDTO.getTaskStatus() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Task status is empty");
        }
    }

    private void populateTask(Task task, TaskDTO taskDTO, SystemUser assignee, TaskStatus taskStatus) {
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setAssignee(assignee);
        if (taskStatus != null) {
            task.setTaskStatus(taskStatus);
        }
    }
}
