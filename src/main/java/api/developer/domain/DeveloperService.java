package api.developer.domain;

import api.infrastructure.model.Specialization;
import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import api.infrastructure.exception.EntityNotFoundException;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public record DeveloperService(DeveloperRepository developerRepository) {

    public DeveloperResponse getDevelopers(UUID userId) throws EntityNotFoundException {
        val developer = developerRepository.findByDeveloperId(userId)
                .orElseThrow(EntityNotFoundException::new);

        return DeveloperResponse.builder()
                .developerId(developer.getUuid())
                .specializations(developer.getSpecialization().name())
                .task(developer.getTask())
                .projects(developer.getProjects())
                .build();
    }


    public void addDeveloper(DeveloperRequest developerRequest) throws IllegalArgumentException {
        val developer = Developer.builder()
                .specialization(Specialization.valueOf(developerRequest.specialization()))
                .build();
        developerRepository.save(developer);
    }
}
