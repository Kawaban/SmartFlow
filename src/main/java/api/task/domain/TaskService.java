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
                   FibonacciChecker fibonacciChecker, TaskMapper taskMapper) implements api.task.TaskService {

    @Override
    public TaskResponse getTask(UUID taskId) throws EntityNotFoundException {
        return taskRepository.findById(taskId)
                .map(taskMapper::toTaskResponse)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void addTask(UUID projectId, TaskRequest taskRequest) throws EntityNotFoundException, IllegalArgumentException {

        if (!fibonacciChecker.isFibonacci(taskRequest.estimation())) {
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

    @Override
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

    @Override
    public Task findByTaskId(UUID taskId) throws EntityNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void updateTask(Task task) throws OptimisticLockException {
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID projectId, UUID taskId) throws EntityNotFoundException {
        Task task = taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponse> getAllTasks(UUID projectId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getProject().getUuid().equals(projectId))
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getAllTasksForUser(UUID projectId, UUID userId) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getProject().getUuid().equals(projectId))
                .filter(task -> task.getAssignedTo().getUuid().equals(userId))
                .map(taskMapper::toTaskResponse)
                .toList();
    }


}
