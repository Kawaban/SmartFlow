package api.developer;

import api.developer.domain.Developer;
import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

import java.util.List;
import java.util.UUID;

public interface DeveloperService {
    DeveloperResponse getDevelopers(UUID userId) throws EntityNotFoundException;

    void addDeveloper(DeveloperRequest developerRequest) throws IllegalArgumentException;

    Developer loadDeveloperByUsername(String username) throws EntityNotFoundException;

    Developer findByDeveloperId(UUID userId) throws EntityNotFoundException;

    void updateDeveloper(Developer developer) throws OptimisticLockException;

    List<DeveloperResponse> getAllDevelopers();
}
