package api.project.dto;

import api.developer.domain.Developer;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProjectRequest(@NotNull String projectName, List<UUID> developers) {
}
