package api.task.domain;

import api.infrastructure.exception.EntityNotFoundException;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public record TaskController(TaskService taskService) {
    @GetMapping("/{taskId}")
    public TaskResponse getTask(@PathVariable UUID projectId, @PathVariable UUID taskId) throws EntityNotFoundException {
        return taskService.getTask(taskId);
    }

    @PostMapping
    public void addTask(@PathVariable UUID projectId, @RequestBody TaskRequest taskRequest) throws EntityNotFoundException, IllegalArgumentException {
        taskService.addTask(projectId, taskRequest);
    }

    @PutMapping("/{taskId}")
    public void updateTaskStatus(@PathVariable UUID projectId, @PathVariable UUID taskId, @RequestBody TaskChange taskChange) throws EntityNotFoundException, IllegalArgumentException, OptimisticLockException {
        taskService.updateTaskStatus(projectId, taskId, taskChange);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable UUID projectId, @PathVariable UUID taskId) throws EntityNotFoundException {
        taskService.deleteTask(projectId, taskId);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks(@PathVariable UUID projectId) {
        return taskService.getAllTasks(projectId);
    }
}
