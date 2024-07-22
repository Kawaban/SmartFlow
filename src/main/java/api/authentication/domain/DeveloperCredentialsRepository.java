package api.authentication.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface DeveloperCredentialsRepository extends JpaRepository<DeveloperCredentials, UUID> {
    Optional<DeveloperCredentials> findByUsername(String username);

    Boolean existsByUsername(String username);
}
