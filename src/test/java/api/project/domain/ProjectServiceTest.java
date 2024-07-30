package api.project.domain;

import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.domain.ProjectService;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import api.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DeveloperService developerService;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void testGetProject_ProjectExists() throws EntityNotFoundException {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);
        project.setName("Test Project");

        Task task1 = new Task();
        task1.setUuid(UUID.randomUUID());
        Task task2 = new Task();
        task2.setUuid(UUID.randomUUID());
        List<Task> tasks = List.of(task1, task2);
        project.setTasks(tasks);

        Developer developer1 = new Developer();
        developer1.setUuid(UUID.randomUUID());
        Developer developer2 = new Developer();
        developer2.setUuid(UUID.randomUUID());
        List<Developer> developers = List.of(developer1, developer2);
        project.setProjectDevelopers(developers);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getProject(projectId);

        assertNotNull(response);
        assertEquals(projectId, response.projectId());
        assertEquals("Test Project", response.projectName());
        assertEquals(2, response.tasks().size());
        assertEquals(2, response.developers().size());
    }

    @Test
    void testGetProject_ProjectDoesNotExist() {
        UUID projectId = UUID.randomUUID();
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.getProject(projectId));
    }

    @Test
    void testAddProject_ValidProject() {
        ProjectRequest projectRequest = new ProjectRequest("New Project", List.of(UUID.randomUUID(), UUID.randomUUID()));

        Developer developer1 = new Developer();
        developer1.setUuid(UUID.randomUUID());
        Developer developer2 = new Developer();
        developer2.setUuid(UUID.randomUUID());

        when(developerService.findByDeveloperId(any(UUID.class))).thenReturn(developer1, developer2);

        projectService.addProject(projectRequest);

        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testFindByProjectId_ProjectExists() throws EntityNotFoundException {
        UUID projectId = UUID.randomUUID();
        Project project = new Project();
        project.setUuid(projectId);

        when(projectRepository.findByUuid(projectId)).thenReturn(Optional.of(project));

        Project foundProject = projectService.findByProjectId(projectId);

        assertNotNull(foundProject);
        assertEquals(projectId, foundProject.getUuid());
    }

    @Test
    void testFindByProjectId_ProjectDoesNotExist() {
        UUID projectId = UUID.randomUUID();

        when(projectRepository.findByUuid(projectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> projectService.findByProjectId(projectId));
    }
}
