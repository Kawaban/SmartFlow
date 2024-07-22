package api.task;

import api.infrastructure.exception.EntityNotFoundException;
import api.task.domain.Task;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import jakarta.persistence.OptimisticLockException;

import java.util.UUID;

public interface TaskService {
    TaskResponse getTask(UUID taskId) throws EntityNotFoundException;

    void addTask(UUID projectId, TaskRequest taskRequest) throws EntityNotFoundException, IllegalArgumentException;

    void updateTaskStatus(UUID projectId, UUID taskId, TaskChange taskChange) throws EntityNotFoundException, OptimisticLockException, IllegalArgumentException;

    Task findByTaskId(UUID taskId) throws EntityNotFoundException;

    void updateTask(Task task) throws OptimisticLockException;
}
