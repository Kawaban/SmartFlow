package baseObjects;

import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @Column(name = "endAt")
    private LocalDateTime endAt;
    @Enumerated(EnumType.STRING)
    @Column(name ="taskState")
    private TaskState taskState;
    @Column(name = "estimation")
    private int estimation;

    public TaskLog(long id, long projectId, Developer developer, LocalDateTime createdAt, LocalDateTime endAt, TaskState taskState, int estimation) {
        this.id = id;
        this.projectId = projectId;
        this.developer = developer;
        this.createdAt = createdAt;
        this.endAt = endAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
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
        obj.put("createdAt",createdAt.toString());
        obj.put("endAt",endAt.toString());
        obj.put("taskState",taskState.toString());
        obj.put("estimation",estimation);
        return obj;
    }

}
