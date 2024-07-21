package api.project.domain;

import api.infrastructure.exception.EntityNotFoundException;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public record ProjectController(ProjectService projectService) {

    @GetMapping("/{projectId}")
    public ProjectResponse getProject(@PathVariable UUID projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @PostMapping
    public void addProject(@RequestBody ProjectRequest projectResponse){
        projectService.addProject(projectResponse);
    }

}
