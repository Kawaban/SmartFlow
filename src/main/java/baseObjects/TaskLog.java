package baseObjects;

import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="taskLogs")
public class TaskLog {
    @Id
    @Column(name="id")
    private long id;
    @Column(name="projectId")
    private long projectId;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="developer_id")
    private Developer developer;
    @Column(name = "deadline")
    private LocalDateTime deadline;
    @Column(name = "endAt")
    private LocalDateTime endAt;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(name ="taskState")
    private TaskState taskState;
    @Column(name = "estimation")
    private int estimation;

    private final static double CONST_RANK_FAILED=0;
    private final static double CONST_RANK_COMPLETE_HIGH=5.5;
    private final static double CONST_RANK_COMPLETE_LOW=1.0;

    public TaskLog(long id, long projectId, Developer developer, LocalDateTime deadline, LocalDateTime endAt, LocalDateTime createdAt, TaskState taskState, int estimation) {
        this.id = id;
        this.projectId = projectId;
        this.developer = developer;
        this.deadline = deadline;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.taskState = taskState;
        this.estimation = estimation;
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getEndAt() {
        return endAt;

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setEndAt(LocalDateTime endAt) {
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

    public JSONObject ToJSONObject()
    {
        JSONObject obj=new JSONObject();
        obj.put("id",id);
        obj.put("projectId",projectId);
        obj.put("createdAt",deadline.toString());
        obj.put("endAt",endAt.toString());
        obj.put("taskState",taskState.toString());
        obj.put("estimation",estimation);
        obj.put("createdAt",createdAt.toString());
        return obj;
    }

    public Double calculateRank()
    {
        if(taskState==TaskState.FAILED)
            return CONST_RANK_FAILED;

        long deltaTime= ChronoUnit.DAYS.between(endAt,deadline);
        long taskTime=ChronoUnit.DAYS.between(createdAt,deadline);
        double percent=deltaTime/taskTime;
        return CONST_RANK_COMPLETE_HIGH - percent*(CONST_RANK_COMPLETE_HIGH-CONST_RANK_COMPLETE_LOW);
    }

}
