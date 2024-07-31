package api.project.domain;

import api.infrastructure.exception.EntityNotFoundException;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public record ProjectController(ProjectService projectService) {

    @GetMapping("/{projectId}")
    public ProjectResponse getProject(@PathVariable UUID projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @PostMapping
    public void addProject(@RequestBody ProjectRequest projectResponse) {
        projectService.addProject(projectResponse);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable UUID projectId) throws EntityNotFoundException {
        projectService.deleteProject(projectId);
    }

    @PutMapping("/{projectId}")
    public void updateProject(@PathVariable UUID projectId, @RequestBody ProjectRequest projectRequest) throws EntityNotFoundException {
        projectService.updateProject(projectId, projectRequest);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

}
