package api.project.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByUuid(UUID projectId);
}
