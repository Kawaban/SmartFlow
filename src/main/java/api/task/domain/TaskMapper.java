package api.task.domain;

import api.developer.DeveloperService;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.ProjectService;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
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
                .build();
    }

}
