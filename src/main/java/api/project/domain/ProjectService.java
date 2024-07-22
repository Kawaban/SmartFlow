package api.project.domain;

import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
record ProjectService(ProjectRepository projectRepository) implements api.project.ProjectService {

    public ProjectResponse getProject(UUID projectId) throws EntityNotFoundException {
        val project = projectRepository.findById(projectId)
                .orElseThrow(EntityNotFoundException::new);

        return ProjectResponse.builder()
                .projectId(project.getUuid())
                .projectName(project.getName())
                .tasks(project.getTasks())
                .developers(project.getProjectDevelopers())
                .build();

    }

    public void addProject(ProjectRequest projectRequest) {
        val project = Project.builder()
                .name(projectRequest.projectName())
                .projectDevelopers((ArrayList<Developer>) projectRequest.developers())
                .build();
        projectRepository.save(project);
    }

    public Project findByProjectId(UUID projectId) throws EntityNotFoundException {
        return projectRepository.findByProjectId(projectId).orElseThrow(EntityNotFoundException::new);
    }

}
