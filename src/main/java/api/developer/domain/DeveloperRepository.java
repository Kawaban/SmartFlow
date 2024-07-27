package api.developer.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
interface DeveloperRepository extends JpaRepository<Developer, UUID> {
    Optional<Developer> findByUuid(UUID userId);

    Optional<Developer> findByUsername(String username);
}
