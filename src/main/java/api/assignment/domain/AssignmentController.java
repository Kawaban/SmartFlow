package api.assignment.domain;

import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;
import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/assignments")
public record AssignmentController(AssignmentService assignmentService) {

    @GetMapping
    public ArrayList<AssignmentResponse> getProposalForAssigningTasks(@PathVariable UUID projectId) throws EntityNotFoundException {
        return assignmentService.delegateTasks(projectId);
    }

    @PostMapping("/{assignmentId}")
    public void setAssignment(@PathVariable UUID projectId,@PathVariable UUID assignmentId,@RequestBody AssignmentDecision assignmentDecision) throws EntityNotFoundException {
        assignmentService.setAssignment(assignmentId, assignmentDecision);
    }
}
