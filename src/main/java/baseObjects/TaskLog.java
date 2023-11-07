package baseObjects;

import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "task_logs")
public class TaskLog {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "project_id")
    private long projectId;
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

    public TaskLog(long id, long projectId, Developer developer, LocalDate deadline, LocalDate endAt, LocalDate createdAt, TaskState taskState, int estimation) {
        this.id = id;
        this.projectId = projectId;
        this.developer = developer;
        this.deadline = deadline;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.taskState = taskState;
        this.estimation = estimation;
    }

    public TaskLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getEndAt() {
        return endAt;

    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public JSONObject ToJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("projectId", projectId);
        obj.put("createdAt", deadline.toString());
        obj.put("endAt", endAt.toString());
        obj.put("taskState", taskState.toString());
        obj.put("estimation", estimation);
        obj.put("createdAt", createdAt.toString());
        return obj;
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
