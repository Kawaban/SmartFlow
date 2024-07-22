package api.project;

import api.infrastructure.exception.EntityNotFoundException;
import api.project.domain.Project;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;

import java.util.UUID;

public interface ProjectService {
    ProjectResponse getProject(UUID projectId) throws EntityNotFoundException;

    void addProject(ProjectRequest projectRequest);

    Project findByProjectId(UUID projectId) throws EntityNotFoundException;
}
