package api.task.domain;

import api.developer.DeveloperService;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.FibonacciChecker;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.ProjectService;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
record TaskService(TaskRepository taskRepository, DeveloperService developerService,
                   ProjectService projectService, TaskLogRepository taskLogRepository,
                   FibonacciChecker fibonacciChecker) implements api.task.TaskService {

    public TaskResponse getTask(UUID taskId) throws EntityNotFoundException {
        return taskRepository.findById(taskId)
                .map(task -> TaskResponse.builder()
                        .taskId(task.getUuid())
                        .name(task.getName())
                        .description(task.getDescription())
                        .status(String.valueOf(task.getTaskState()))
                        .createdBy(String.valueOf(task.getCreatedBy()))
                        .createdAt(String.valueOf(task.getCreatedAt()))
                        .deadline(String.valueOf(task.getDeadline()))
                        .assignedTo(task.getAssignedTo().getUuid())
                        .estimation(task.getEstimation())
                        .specialization(String.valueOf(task.getSpecialization()))
                        .projectId(task.getProject().getUuid())
                        .build())
                .orElseThrow(EntityNotFoundException::new);
    }

    public void addTask(UUID projectId, TaskRequest taskRequest) throws EntityNotFoundException, IllegalArgumentException {

        if (fibonacciChecker.isFibonacci(taskRequest.estimation())) {
            throw new IllegalArgumentException("Estimation is not a fibonacci number");
        }

        Task task = Task.builder().
                name(taskRequest.name()).
                description(taskRequest.description()).
                taskState(TaskState.valueOf(taskRequest.status())).
                createdBy(taskRequest.createdBy()).
                createdAt(LocalDate.parse(taskRequest.createdAt())).
                deadline(LocalDate.parse(taskRequest.deadline())).
                assignedTo(developerService.findByDeveloperId(taskRequest.assignedTo())).
                estimation(taskRequest.estimation()).
                specialization(Specialization.valueOf(taskRequest.specialization())).
                project(projectService.findByProjectId(projectId)).
                build();
        taskRepository.save(task);
    }

    public void updateTaskStatus(UUID projectId, UUID taskId, TaskChange taskChange) throws EntityNotFoundException, OptimisticLockException, IllegalArgumentException {
        Task task = taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
        task.setTaskState(TaskState.valueOf(taskChange.status()));

        if (TaskState.valueOf(taskChange.status()) == TaskState.COMPLETED || TaskState.valueOf(taskChange.status()) == TaskState.FAILED) {
            TaskLog taskLog = TaskLog.builder()
                    .endAt(LocalDate.now())
                    .createdAt(task.getCreatedAt())
                    .deadline(task.getDeadline())
                    .developer(task.getAssignedTo())
                    .estimation(task.getEstimation())
                    .projectId(task.getProject().getUuid())
                    .name(task.getName())
                    .description(task.getDescription())
                    .taskState(TaskState.valueOf(taskChange.status()))
                    .build();

            taskLogRepository.save(taskLog);

            task.setAssignedTo(null);
            taskRepository.delete(task);
        }

        taskRepository.save(task);
    }

    public Task findByTaskId(UUID taskId) throws EntityNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
    }

    public void updateTask(Task task) throws OptimisticLockException {
        taskRepository.save(task);
    }

    public void deleteTask(UUID projectId, UUID taskId) throws EntityNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
        taskRepository.delete(task);
    }

    public List<TaskResponse> getAllTasks(UUID projectId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getProject().getUuid().equals(projectId))
                .map(task -> TaskResponse.builder()
                        .taskId(task.getUuid())
                        .name(task.getName())
                        .description(task.getDescription())
                        .status(String.valueOf(task.getTaskState()))
                        .createdBy(String.valueOf(task.getCreatedBy()))
                        .createdAt(String.valueOf(task.getCreatedAt()))
                        .deadline(String.valueOf(task.getDeadline()))
                        .assignedTo(task.getAssignedTo().getUuid())
                        .estimation(task.getEstimation())
                        .specialization(String.valueOf(task.getSpecialization()))
                        .projectId(task.getProject().getUuid())
                        .build())
                .toList();
    }

    @Override
    public List<TaskResponse> getAllTasksForUser(UUID projectId, UUID userId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getProject().getUuid().equals(projectId))
                .filter(task -> task.getAssignedTo().getUuid().equals(userId))
                .map(task -> TaskResponse.builder()
                        .taskId(task.getUuid())
                        .name(task.getName())
                        .description(task.getDescription())
                        .status(String.valueOf(task.getTaskState()))
                        .createdBy(String.valueOf(task.getCreatedBy()))
                        .createdAt(String.valueOf(task.getCreatedAt()))
                        .deadline(String.valueOf(task.getDeadline()))
                        .assignedTo(task.getAssignedTo().getUuid())
                        .estimation(task.getEstimation())
                        .specialization(String.valueOf(task.getSpecialization()))
                        .projectId(task.getProject().getUuid())
                        .build())
                .toList();
    }


}
