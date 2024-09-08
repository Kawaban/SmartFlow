package api.task.domain;

import api.infrastructure.exception.EntityNotFoundException;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
class TaskController {
    private final TaskService taskService;

    @GetMapping("/{taskId}")
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.createdBy == authentication.principal.uuid or returnObject.assignedTo == authentication.principal.uuid")
    public TaskResponse getTask(@PathVariable UUID projectId, @PathVariable UUID taskId) throws EntityNotFoundException {
        return taskService.getTask(taskId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or #taskRequest.createdBy == authentication.principal.uuid")
    public void addTask(@PathVariable UUID projectId, @RequestBody TaskRequest taskRequest) throws EntityNotFoundException, IllegalArgumentException {
        taskService.addTask(projectId, taskRequest);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @taskService.getTask(#taskId).createdBy == authentication.principal.uuid")
    public void updateTaskStatus(@PathVariable UUID projectId, @PathVariable UUID taskId, @RequestBody TaskChange taskChange) throws EntityNotFoundException, IllegalArgumentException, OptimisticLockException {
        taskService.updateTaskStatus(projectId, taskId, taskChange);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @taskService.getTask(#taskId).createdBy == authentication.principal.uuid")
    public void deleteTask(@PathVariable UUID projectId, @PathVariable UUID taskId) throws EntityNotFoundException {
        taskService.deleteTask(projectId, taskId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TaskResponse> getAllTasks(@PathVariable UUID projectId) {
        return taskService.getAllTasks(projectId);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.uuid")
    public List<TaskResponse> getAllTasksForUser(@PathVariable UUID projectId, @PathVariable UUID userId) {
        return taskService.getAllTasksForUser(projectId, userId);
    }
}
