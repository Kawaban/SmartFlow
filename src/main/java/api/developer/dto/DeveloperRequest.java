package api.developer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeveloperRequest(@NotNull String specialization, @NotNull String login) {
}
