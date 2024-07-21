package api.assignment.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AssignmentResponse(UUID taskId, UUID developerId, UUID assignmentId) {
}
