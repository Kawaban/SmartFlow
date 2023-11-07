package baseObjects;

import additionalObjects.Rank;
import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "developers")
public class Developer {
    @Id
    @Column(name = "id")
    private long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "projects_developers",
            joinColumns = @JoinColumn(name = "developer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    @OneToMany(mappedBy = "developer", fetch = FetchType.LAZY)
    private List<TaskLog> tasksLogs;

    public Developer(long id, Specialization specialization) {
        this.id = id;
        this.specialization = specialization;
    }


    public Developer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
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

    public List<TaskLog> getTasksLogs() {
        return tasksLogs;
    }

    public void setTasksLogs(ArrayList<TaskLog> tasksLogs) {
        this.tasksLogs = tasksLogs;
    }

    public JSONObject ToJSONObject(Boolean extendedInfo) {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("specialization", specialization.toString());
        if (extendedInfo) {
            if (projects != null) {
                ArrayList<JSONObject> projectsJSON = new ArrayList<JSONObject>();
                for (Project project : projects)
                    projectsJSON.add(project.ToJSONObject(false));
                obj.put("projects", projectsJSON);
            } else
                obj.put("projects", "null");
        }

        if (task != null)
            obj.put("task", task.getId());
        else
            obj.put("task", "null");


        return obj;

    }

    public Rank calculateRank(int estimation) {
        Rank rank = new Rank(estimation, id);
        for (TaskLog taskLog : tasksLogs) {
            if (taskLog.getEstimation() == estimation)
                rank.updateRank(taskLog.calculateRank());
        }
        if (rank.getPower() == 0)
            rank.setDefaultRank();
        return rank;
    }
}
