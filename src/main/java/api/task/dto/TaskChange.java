package api.task.dto;

import jakarta.validation.constraints.NotNull;

public record TaskChange(@NotNull String status) {
}
