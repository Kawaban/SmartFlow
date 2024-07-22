package api.project.dto;

import api.developer.domain.Developer;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectRequest(@NotNull String projectName, List<Developer> developers) {
}
