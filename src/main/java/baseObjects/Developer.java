package baseObjects;

import additionalObjects.Rank;
import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;


import javax.persistence.*;
import java.util.ArrayList;
@Entity
@Table(name="developers")
public class Developer  {
    @Id
    @Column(name="id")
    private long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project;
    @OneToOne
    @JoinColumn(name="task_id")
    private Task task;
    @Enumerated(EnumType.STRING)
    @Column(name="specialization")
    private Specialization specialization;

    @OneToMany(mappedBy = "developer",fetch=FetchType.LAZY)
    private ArrayList<TaskLog> tasksLogs;

    public Developer(long id, Specialization specialization) {
        this.id = id;
        this.specialization = specialization;
    }

    public Developer(long id, Project project, Specialization specialization) {
        this.id = id;
        this.project = project;
        this.specialization = specialization;
    }

    public Developer(long id, Project project, Task task, Specialization specialization) {
        this.id = id;
        this.project = project;
        this.task = task;
        this.specialization = specialization;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public ArrayList<TaskLog> getTasksLogs() {
        return tasksLogs;
    }

    public void setTasksLogs(ArrayList<TaskLog> tasksLogs) {
        this.tasksLogs = tasksLogs;
    }

    public JSONObject ToJSONObject(Boolean extendedInfo)
    {
        JSONObject obj=new JSONObject();
        obj.put("id",id);
        obj.put("specialization",specialization.toString());

        if(project!=null)
            obj.put("project",project.getId());
        else
            obj.put("project","null");

        if(task!=null)
            obj.put("task",task.getId());
        else
            obj.put("task","null");


        if(extendedInfo && tasksLogs!=null)
        {
            ArrayList<JSONObject> taskLogsJSON=new ArrayList<JSONObject>();
            for(TaskLog taskLog:tasksLogs)
                taskLogsJSON.add(taskLog.ToJSONObject());
        }

        return obj;

    }

    public ArrayList<Rank> calculateRanks()
    {
        ArrayList<Rank> ranks=new ArrayList<Rank>();
        for(TaskLog taskLog:tasksLogs)
        {
            if(taskLog.getTaskState()!= TaskState.SKIPPED) {
                int i = ranks.indexOf(new Rank(taskLog.getEstimation(), 0,null));
                ranks.get(i).updateRank(taskLog.calculateRank());
            }
        }
        return ranks;
    }
}
