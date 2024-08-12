package api.assignment.domain;

import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;
import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.ProjectService;
import api.project.domain.Project;
import api.task.TaskService;
import api.task.domain.Task;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private Algorithm algorithm;

    @Mock
    private ProjectService projectService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private DeveloperService developerService;

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    void testSetAssignment_Accepted() throws EntityNotFoundException, OptimisticLockException {
        UUID assignmentId = UUID.randomUUID();
        AssignmentDecision decision = new AssignmentDecision(true);
        Assignment assignment = Assignment.builder()
                .id(assignmentId)
                .taskId(UUID.randomUUID())
                .developerId(UUID.randomUUID())
                .build();

        Task task = new Task();
        task.setUuid(assignment.getTaskId());
        task.setTaskState(TaskState.DEFAULT);

        Developer developer = new Developer();
        developer.setUuid(assignment.getDeveloperId());
        developer.setSpecialization(Specialization.BACKEND);

        when(assignmentRepository.findByUuid(assignmentId)).thenReturn(Optional.of(assignment));
        when(taskService.findByTaskId(assignment.getTaskId())).thenReturn(task);
        when(developerService.findByDeveloperId(assignment.getDeveloperId())).thenReturn(developer);

        assignmentService.setAssignment(assignmentId, decision);

        verify(taskService, times(1)).updateTask(task);
        verify(developerService, times(1)).updateDeveloper(developer);
        assertEquals(developer, task.getAssignedTo());
        assertEquals(task, developer.getTask());
        assertEquals(TaskState.ASSIGNED, task.getTaskState());
    }

    @Test
    void testSetAssignment_Rejected() throws EntityNotFoundException, OptimisticLockException {
        UUID assignmentId = UUID.randomUUID();
        AssignmentDecision decision = new AssignmentDecision(false);
        Assignment assignment = Assignment.builder()
                .id(assignmentId)
                .taskId(UUID.randomUUID())
                .developerId(UUID.randomUUID())
                .build();

        when(assignmentRepository.findByUuid(assignmentId)).thenReturn(Optional.of(assignment));

        assignmentService.setAssignment(assignmentId, decision);

        verify(assignmentRepository, times(1)).delete(assignment);
        verify(taskService, never()).updateTask(any(Task.class));
        verify(developerService, never()).updateDeveloper(any(Developer.class));
    }

    @Test
    void testSetAssignment_EntityNotFound() {
        UUID assignmentId = UUID.randomUUID();
        AssignmentDecision decision = new AssignmentDecision(true);

        when(assignmentRepository.findByUuid(assignmentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> assignmentService.setAssignment(assignmentId, decision));
    }

    @Test
    void testDelegateTasks() throws EntityNotFoundException {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);
        project.setTasks(new ArrayList<>());
        project.setProjectDevelopers(new ArrayList<>());

        when(projectService.findByProjectId(projectId)).thenReturn(project);

        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(Assignment.builder()
                .taskId(UUID.randomUUID())
                .developerId(UUID.randomUUID())
                .id(UUID.randomUUID())
                .build());
        when(algorithm.delegate(any(ArrayList.class), any(ArrayList.class))).thenReturn(assignments);

        List<AssignmentResponse> responses = assignmentService.delegateTasks(projectId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(assignments.get(0).getTaskId(), responses.get(0).taskId());
        assertEquals(assignments.get(0).getDeveloperId(), responses.get(0).developerId());
        assertEquals(assignments.get(0).getUuid(), responses.get(0).assignmentId());
    }
}