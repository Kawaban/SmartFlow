package api.project.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByProjectId(UUID projectId);
}
