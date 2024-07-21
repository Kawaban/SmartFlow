package api.task.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record TaskRequest(@NonNull String name,
                          @NonNull String description,
                          @NonNull String status,
                          @NonNull UUID createdBy,
                          @NonNull String createdAt,
                          @NonNull String deadline,
                          @NonNull int estimation,
                          @NonNull String specialization,
                          UUID assignedTo) {
}
