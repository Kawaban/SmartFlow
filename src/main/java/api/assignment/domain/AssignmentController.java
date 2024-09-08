package api.assignment.domain;

import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.ProjectService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/assignments")
@RequiredArgsConstructor
class AssignmentController {
    private final AssignmentService assignmentService;
    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectService.getProject(#projectId).developers.contains(authentication.principal.uuid)")
    public ArrayList<AssignmentResponse> getProposalForAssigningTasks(@PathVariable UUID projectId) throws EntityNotFoundException, OptimisticLockException {
        return assignmentService.delegateTasks(projectId);
    }

    @PostMapping("/{assignmentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @projectService.getProject(#projectId).developers.contains(authentication.principal.uuid)")
    public void setAssignment(@PathVariable UUID projectId, @PathVariable UUID assignmentId, @RequestBody AssignmentDecision assignmentDecision) throws EntityNotFoundException {
        assignmentService.setAssignment(assignmentId, assignmentDecision);
    }
}
