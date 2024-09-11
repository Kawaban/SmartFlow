package api.task.domain;

import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.FibonacciChecker;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.ProjectService;
import api.project.domain.Project;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

        @Mock
        private TaskRepository taskRepository;

        @Mock
        private DeveloperService developerService;

        @Mock
        private ProjectService projectService;

        @Mock
        private TaskLogRepository taskLogRepository;

        @Mock
        private FibonacciChecker fibonacciChecker;

        @Mock
        private TaskMapper taskMapper;

        @InjectMocks
        private TaskService taskService;

        private Task testTask;
        private Developer testDeveloper;
        private Project testProject;
        private UUID testTaskId;

        @BeforeEach
        public void setUp() {
                testTaskId = UUID.randomUUID();
                testTask = new Task();
                testTask.setUuid(testTaskId);
                testTask.setName("Test Task");
                testTask.setDescription("Test Description");
                testTask.setTaskState(TaskState.ASSIGNED);
                testDeveloper= new Developer();
                testDeveloper.setUuid(UUID.randomUUID());
                testTask.setAssignedTo(testDeveloper);
                testTask.setCreatedBy(UUID.randomUUID());
                testTask.setCreatedAt(LocalDate.now());
                testTask.setDeadline(LocalDate.now().plusDays(5));
                testProject = new Project();
                testProject.setUuid(UUID.randomUUID());
                testTask.setProject(testProject);
                testTask.setEstimation(5);
                testTask.setSpecialization(Specialization.BACKEND);
        }



        @Test
        public void testGetTask_TaskExists() throws EntityNotFoundException {

                when(taskService.taskRepository().findById(testTaskId)).thenReturn(Optional.of(testTask));
                when(taskService.taskMapper().toTaskResponse(testTask)).thenReturn(new TaskResponse(testTask.getName(), testTask.getDescription(), testTask.getTaskState().toString(), testTask.getCreatedBy(), testTask.getCreatedAt().toString(), testTask.getDeadline().toString(),testTask.getAssignedTo().getUuid(), testTask.getEstimation(), testTask.getSpecialization().toString(),testTask.getProject().getUuid(), testTask.getUuid()));

                TaskResponse response = taskService.getTask(testTaskId);

                assertNotNull(response);
                assertEquals(testTaskId, response.taskId());
                assertEquals("Test Task", response.name());
                assertEquals("Test Description", response.description());
                assertEquals("ASSIGNED", response.status());
                assertEquals(testTask.getAssignedTo().getUuid(), response.assignedTo());
        }

        @Test
        public void testGetTask_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();
                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.getTask(taskId));
        }

        @Test
        public void testAddTask_ValidTask() throws EntityNotFoundException {
                UUID projectId = UUID.randomUUID();
                TaskRequest taskRequest = new TaskRequest("Test Task", "Test Description", "COMPLETED", UUID.randomUUID(), "2023-07-27", "2023-07-30", 5, "BACKEND", UUID.randomUUID());

                when(fibonacciChecker.isFibonacci(taskRequest.estimation())).thenReturn(true);
                when(developerService.findByDeveloperId(taskRequest.assignedTo())).thenReturn(testDeveloper);
                when(projectService.findByProjectId(projectId)).thenReturn(testProject);

                taskService.addTask(projectId, taskRequest);

                verify(taskService.taskRepository(), times(1)).save(any(Task.class));
        }

        @Test
        public void testAddTask_InvalidEstimation() {
                UUID projectId = UUID.randomUUID();
                TaskRequest taskRequest = new TaskRequest("Test Task", "Test Description", "IN_PROGRESS", UUID.randomUUID(), "2023-07-27", "2023-07-30", 5, "BACKEND", UUID.randomUUID());

                when(fibonacciChecker.isFibonacci(taskRequest.estimation())).thenReturn(false);

                assertThrows(IllegalArgumentException.class, () -> taskService.addTask(projectId, taskRequest));
        }

        @Test
        public void testUpdateTaskStatus_TaskExists() throws EntityNotFoundException {

                TaskChange taskChange = new TaskChange("COMPLETED");

                when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));
                taskService.updateTaskStatus(testProject.getUuid(),testTaskId, taskChange);

                verify(taskLogRepository, times(1)).save(any(TaskLog.class));
                verify(taskRepository, times(1)).delete(testTask);
                verify(taskRepository, times(1)).save(any(Task.class));
        }

        @Test
        public void testUpdateTaskStatus_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();
                TaskChange taskChange = new TaskChange("COMPLETED");

                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.updateTaskStatus(UUID.randomUUID(), taskId, taskChange));
        }

        @Test
        public void testFindByTaskId_TaskExists() throws EntityNotFoundException {
                when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));

                Task foundTask = taskService.findByTaskId(testTaskId);

                assertNotNull(foundTask);
                assertEquals(testTaskId, foundTask.getUuid());
        }

        @Test
        public void testFindByTaskId_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();

                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.findByTaskId(taskId));
        }

        @Test
        public void testUpdateTask() {
                taskService.updateTask(testTask);

                verify(taskService.taskRepository(), times(1)).save(testTask);
        }

}
