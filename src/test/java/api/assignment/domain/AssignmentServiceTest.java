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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

        Task task = new Task();
        task.setUuid(UUID.randomUUID());
        task.setTaskState(TaskState.DEFAULT);
        task.setSpecialization(Specialization.BACKEND);

        Task task2 = new Task();
        task2.setUuid(UUID.randomUUID());
        task2.setTaskState(TaskState.DEFAULT);
        task2.setSpecialization(Specialization.BACKEND);

        Developer developer = new Developer();
        developer.setUuid(UUID.randomUUID());
        developer.setSpecialization(Specialization.BACKEND);

        Developer developer2 = new Developer();
        developer2.setUuid(UUID.randomUUID());
        developer2.setSpecialization(Specialization.BACKEND);


        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);
        project.setTasks(tasks);

        ArrayList<Developer> developers = new ArrayList<>();
        developers.add(developer);
        developers.add(developer2);
        project.setProjectDevelopers(developers);

        when(projectService.findByProjectId(projectId)).thenReturn(project);

        ArrayList<Assignment> assignments = new ArrayList<>();
        assignments.add(Assignment.builder()
                .taskId(task.getUuid())
                .developerId(developer.getUuid())
                .id(UUID.randomUUID())
                .build());

        assignments.add(Assignment.builder()
                .taskId(task2.getUuid())
                .developerId(developer2.getUuid())
                .id(UUID.randomUUID())
                .build());


        lenient().when(algorithm.delegate(tasks, developers)).thenReturn(assignments);

        List<AssignmentResponse> responses = assignmentService.delegateTasks(projectId);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(assignments.getFirst().getTaskId(), responses.getFirst().taskId());
        assertEquals(assignments.getFirst().getDeveloperId(), responses.getFirst().developerId());
        assertEquals(assignments.getFirst().getUuid(), responses.getFirst().assignmentId());
    }
}