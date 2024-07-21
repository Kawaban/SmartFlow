package api.task.domain;


import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.developer.domain.Developer;
import api.infrastructure.model.AbstractEntity;
import api.project.domain.Project;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "created_by")
    private UUID createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_state")
    private TaskState taskState;
    @Column(name = "name")
    private String name;
    @Column(name = "estimation")
    private int estimation;
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
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
