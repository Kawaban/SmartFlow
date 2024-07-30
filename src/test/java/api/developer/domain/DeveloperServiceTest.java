package api.developer.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.domain.Project;
import api.task.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import api.infrastructure.model.*;


@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    void testGetDevelopers_DeveloperExists() throws EntityNotFoundException {
        UUID userId = UUID.randomUUID();
        Developer developer = new Developer();
        developer.setUuid(userId);
        developer.setSpecialization(Specialization.BACKEND);

        Project project1 = new Project();
        project1.setUuid(UUID.randomUUID());
        Project project2 = new Project();
        project2.setUuid(UUID.randomUUID());
        developer.setProjects(List.of(project1, project2));

        Task task = new Task();
        task.setUuid(UUID.randomUUID());
        developer.setTask(task);

        when(developerRepository.findByUuid(userId)).thenReturn(Optional.of(developer));

        DeveloperResponse response = developerService.getDevelopers(userId);

        assertNotNull(response);
        assertEquals(userId, response.developerId());
        assertEquals("BACKEND", response.specializations());
        assertEquals(task.getUuid(), response.task());
        assertEquals(2, response.projects().size());
    }

    @Test
    void testGetDevelopers_DeveloperDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(developerRepository.findByUuid(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> developerService.getDevelopers(userId));
    }

    @Test
    void testAddDeveloper_ValidDeveloper() {
        DeveloperRequest developerRequest = new DeveloperRequest("BACKEND", "developerUsername");

        developerService.addDeveloper(developerRequest);

        verify(developerRepository, times(1)).save(any(Developer.class));
    }

    @Test
    void testAddDeveloper_InvalidSpecialization() {
        DeveloperRequest developerRequest = new DeveloperRequest("INVALID_SPECIALIZATION", "developerUsername");

        assertThrows(IllegalArgumentException.class, () -> developerService.addDeveloper(developerRequest));
    }

    @Test
    void testLoadDeveloperByUsername_DeveloperExists() throws EntityNotFoundException {
        String username = "developerUsername";
        Developer developer = new Developer();
        developer.setUsername(username);

        when(developerRepository.findByUsername(username)).thenReturn(Optional.of(developer));

        Developer foundDeveloper = developerService.loadDeveloperByUsername(username);

        assertNotNull(foundDeveloper);
        assertEquals(username, foundDeveloper.getUsername());
    }

    @Test
    void testLoadDeveloperByUsername_DeveloperDoesNotExist() {
        String username = "developerUsername";

        when(developerRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> developerService.loadDeveloperByUsername(username));
    }

    @Test
    void testFindByDeveloperId_DeveloperExists() throws EntityNotFoundException {
        UUID userId = UUID.randomUUID();
        Developer developer = new Developer();
        developer.setUuid(userId);

        when(developerRepository.findByUuid(userId)).thenReturn(Optional.of(developer));

        Developer foundDeveloper = developerService.findByDeveloperId(userId);

        assertNotNull(foundDeveloper);
        assertEquals(userId, foundDeveloper.getUuid());
    }

    @Test
    void testFindByDeveloperId_DeveloperDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(developerRepository.findByUuid(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> developerService.findByDeveloperId(userId));
    }

    @Test
    void testUpdateDeveloper() {
        Developer developer = new Developer();
        developer.setUuid(UUID.randomUUID());

        developerService.updateDeveloper(developer);

        verify(developerRepository, times(1)).save(developer);
    }
}