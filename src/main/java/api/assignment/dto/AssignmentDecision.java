package api.assignment.dto;

import jakarta.validation.constraints.NotNull;

public record AssignmentDecision(@NotNull Boolean isAccepted) {
}
