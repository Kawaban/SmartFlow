package api.developer.dto;

import api.project.domain.Project;
import api.task.domain.Task;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DeveloperResponse(
        UUID developerId,
        String username,
        String specializations,
        Task task,
        List<Project> projects
) {
}
