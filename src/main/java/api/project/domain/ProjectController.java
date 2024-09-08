package api.project.domain;

import api.infrastructure.exception.EntityNotFoundException;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
 class ProjectController {
    private final ProjectService projectService;


    @GetMapping("/{projectId}")
    @PostAuthorize("hasRole('ROLE_ADMIN') or (#returnObject.developers.contains(authentication.principal.uuid))")
    public ProjectResponse getProject(@PathVariable UUID projectId) throws EntityNotFoundException {
        return projectService.getProject(projectId);
    }

    @PostMapping
    public void addProject(@RequestBody ProjectRequest projectResponse) {
        projectService.addProject(projectResponse);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectService.getProject(#projectId).developers.contains(authentication.principal.uuid)")
    public void deleteProject(@PathVariable UUID projectId) throws EntityNotFoundException {
        projectService.deleteProject(projectId);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectService.getProject(#projectId).developers.contains(authentication.principal.uuid)")
    public void updateProject(@PathVariable UUID projectId, @RequestBody ProjectRequest projectRequest) throws EntityNotFoundException {
        projectService.updateProject(projectId, projectRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.uuid")
    public List<ProjectResponse> getAllProjectsForUser(@PathVariable UUID userId) {
        return projectService.getAllProjectsForUser(userId);
    }

}
