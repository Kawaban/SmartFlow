package api.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record TaskRequest(@NotNull String name,
                          @NotNull String description,
                          @NotNull String status,
                          @NotNull UUID createdBy,
                          @NotNull String createdAt,
                          @NotNull String deadline,
                          @NotNull int estimation,
                          @NotNull String specialization,
                          UUID assignedTo) {
}
