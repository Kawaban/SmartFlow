package api.task.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record TaskResponse(
        String name,
        String description,
        String status,
        UUID createdBy,
        String createdAt,
        String deadline,
        UUID assignedTo,
        int estimation,
        String specialization,
        UUID projectId,
        UUID taskId
) {
}
