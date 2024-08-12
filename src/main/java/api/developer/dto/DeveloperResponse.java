package api.developer.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record DeveloperResponse(
        UUID developerId,
        String username,
        String specializations,
        UUID task,
        List<UUID> projects
) {
}
