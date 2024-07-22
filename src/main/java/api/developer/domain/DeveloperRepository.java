package api.developer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface DeveloperRepository extends JpaRepository<Developer, UUID> {
    Optional<Developer> findByDeveloperId(UUID userId);

    Optional<Developer> findByUsername(String username);
}
