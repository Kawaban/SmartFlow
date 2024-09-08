package api.task.domain;


import api.developer.domain.Developer;
import api.infrastructure.model.AbstractEntity;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.domain.Project;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "itaskmanager_tasks")
public class Task extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    private UUID createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskState taskState;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int estimation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @OneToOne(mappedBy = "task")
    private Developer assignedTo;

    @Builder
    public Task(UUID id, long version, Instant createdDate, Instant lastModifiedDate, Project project, LocalDate createdAt, LocalDate deadline, UUID createdBy, TaskState taskState, String name, int estimation, Specialization specialization, Developer assignedTo, String description) {
        super(id, version, createdDate, lastModifiedDate);
        this.project = project;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.taskState = taskState;
        this.name = name;
        this.estimation = estimation;
        this.specialization = specialization;
        this.assignedTo = assignedTo;
        this.deadline = deadline;
        this.description = description;
    }


}
