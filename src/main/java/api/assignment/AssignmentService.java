package api.assignment;

import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;

import java.util.ArrayList;
import java.util.UUID;

public interface AssignmentService {
    void setAssignment(UUID assignmentId, AssignmentDecision assignmentDecision);
    ArrayList<AssignmentResponse> delegateTasks(UUID projectId);
}
