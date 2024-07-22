package api.project.dto;

import api.developer.domain.Developer;
import api.task.domain.Task;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProjectResponse(UUID projectId,
                              String projectName,
                              List<Task> tasks,
                              List<Developer> developers) {
}
