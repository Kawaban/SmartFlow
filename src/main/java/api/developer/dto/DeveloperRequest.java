package api.developer.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DeveloperRequest(@NonNull String specialization) {
}
