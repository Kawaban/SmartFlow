package baseObjects;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tech_id")
    private long tech_id;
    @Column(name = "id")
    private long id;

    @Column(name = "project_id")
    private long projectId;
    @Column(name = "developer_id")
    private long developerId;
    @Column(name = "task_id")
    private long taskId;

    public Assignment(long id, long projectId, long developerId, long taskId) {
        this.id = id;
        this.projectId = projectId;
        this.developerId = developerId;
        this.taskId = taskId;
    }

    public Assignment(long developerId, long taskId) {
        this.developerId = developerId;
        this.taskId = taskId;
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

    public long getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(long developerId) {
        this.developerId = developerId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }


    public JSONObject ToJSONObject()
    {
        JSONObject obj=new JSONObject();
        obj.put("id",id);
        obj.put("developerId",developerId);
        obj.put("taskId",taskId);
        return obj;
    }
}
