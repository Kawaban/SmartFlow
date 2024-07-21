package api.task.domain;



import api.infrastructure.model.TaskState;
import api.developer.domain.Developer;
import api.infrastructure.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "task_logs")
public class TaskLog extends AbstractEntity {

    @Column(name = "project_id")
    private UUID projectId;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer developer;
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "end_at")
    private LocalDate endAt;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_state")
    private TaskState taskState;
    @Column(name = "estimation")
    private int estimation;
    @Transient
    private final static double CONST_RANK_FAILED = 0;
    @Transient
    private final static double CONST_RANK_COMPLETE_HIGH = 5.5;
    @Transient
    private final static double CONST_RANK_COMPLETE_LOW = 2.0;

    @Transient
    private final static double CONST_RANK_COMPLETE_EXPIRED = 1.0;

    @Builder
    public TaskLog(UUID id, long version, Instant createdDate, Instant lastModifiedDate, UUID projectId, Developer developer, LocalDate deadline, LocalDate endAt, LocalDate createdAt, TaskState taskState, int estimation, String name, String description) {
        super(id, version, createdDate, lastModifiedDate);
        this.projectId = projectId;
        this.developer = developer;
        this.deadline = deadline;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.taskState = taskState;
        this.estimation = estimation;
        this.name = name;
        this.description = description;
    }

    public Double calculateRank() {
        if (taskState == TaskState.FAILED)
            return CONST_RANK_FAILED;

        long deltaTime = ChronoUnit.DAYS.between(endAt, deadline);
        if (deltaTime < 0)
            return CONST_RANK_COMPLETE_EXPIRED;

        long taskTime = ChronoUnit.DAYS.between(createdAt, deadline);
        if (deltaTime > taskTime)
            return CONST_RANK_COMPLETE_HIGH;
        double percent = deltaTime / taskTime;
        return CONST_RANK_COMPLETE_HIGH - percent * (CONST_RANK_COMPLETE_HIGH - CONST_RANK_COMPLETE_LOW);
    }

}
