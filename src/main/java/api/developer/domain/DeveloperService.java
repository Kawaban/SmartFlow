package api.developer.domain;

import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.Specialization;
import jakarta.persistence.OptimisticLockException;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import api.project.domain.Project;

@Service
record DeveloperService(DeveloperRepository developerRepository) implements api.developer.DeveloperService {

    public DeveloperResponse getDevelopers(UUID userId) throws EntityNotFoundException {
        val developer = developerRepository.findByUuid(userId)
                .orElseThrow(EntityNotFoundException::new);

        List<UUID> projects = developer.getProjects().stream()
                .map(Project::getUuid)
                .toList();

        return DeveloperResponse.builder()
                .developerId(developer.getUuid())
                .specializations(developer.getSpecialization().name())
                .task(developer.getTask().getUuid())
                .projects(projects)
                .build();
    }


    public void addDeveloper(DeveloperRequest developerRequest) throws IllegalArgumentException {
        val developer = Developer.builder()
                .specialization(Specialization.valueOf(developerRequest.specialization()))
                .build();
        developerRepository.save(developer);
    }

    public Developer loadDeveloperByUsername(String username) throws EntityNotFoundException {
        return developerRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public Developer findByDeveloperId(UUID userId) throws EntityNotFoundException {
        return developerRepository.findByUuid(userId).orElseThrow(EntityNotFoundException::new);
    }

    public void updateDeveloper(Developer developer) throws OptimisticLockException {
        developerRepository.save(developer);
    }

}
