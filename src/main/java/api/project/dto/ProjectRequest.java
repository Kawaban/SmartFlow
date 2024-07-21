package api.project.dto;

import api.developer.domain.Developer;
import lombok.NonNull;

import java.util.List;

public record ProjectRequest(@NonNull String projectName, List<Developer> developers) {
}
