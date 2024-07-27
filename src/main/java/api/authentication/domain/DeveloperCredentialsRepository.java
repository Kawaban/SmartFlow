package api.authentication.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
interface DeveloperCredentialsRepository extends JpaRepository<DeveloperCredentials, UUID> {
    Optional<DeveloperCredentials> findByUsername(String username);

    Boolean existsByUsername(String username);
}
