package api.task.domain;

import api.developer.domain.DeveloperRepository;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.domain.ProjectRepository;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public record TaskService(TaskRepository taskRepository, DeveloperRepository developerRepository, ProjectRepository projectRepository, TaskLogRepository taskLogRepository) {
    public TaskResponse getTask(UUID taskId) {
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

    public void addTask(UUID projectId, TaskRequest taskRequest) {
        Task task = Task.builder().
                name(taskRequest.name()).
                description(taskRequest.description()).
                taskState(TaskState.valueOf(taskRequest.status())).
                createdBy(taskRequest.createdBy()).
                createdAt(LocalDate.parse(taskRequest.createdAt())).
                deadline(LocalDate.parse(taskRequest.deadline())).
                assignedTo(developerRepository.findByDeveloperId(taskRequest.assignedTo()).orElseThrow(EntityNotFoundException::new)).
                estimation(taskRequest.estimation()).
                specialization(Specialization.valueOf(taskRequest.specialization())).
                project(projectRepository.findByProjectId(projectId).orElseThrow(EntityNotFoundException::new)).
                build();
        taskRepository.save(task);
    }

    public void updateTaskStatus(UUID projectId, UUID taskId, TaskChange taskChange) {
        Task task = taskRepository.findById(taskId).orElseThrow(EntityNotFoundException::new);
        task.setTaskState(TaskState.valueOf(taskChange.status()));

        if(TaskState.valueOf(taskChange.status()) == TaskState.COMPLETED || TaskState.valueOf(taskChange.status()) == TaskState.FAILED) {
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



}
