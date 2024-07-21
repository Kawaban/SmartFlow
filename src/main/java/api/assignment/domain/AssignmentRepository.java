package api.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    Optional<Assignment> findByAssignmentId(UUID assignmentId);
}
