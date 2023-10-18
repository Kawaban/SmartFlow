package baseObjects;

import additionalObjects.Specialization;

import java.util.ArrayList;

public class Developer {
    private String id;
    private String projectId;
    private String taskId;
    private Specialization specialization;
    private ArrayList<Task> tasksLog;

    public Developer(String id, Specialization specialization) {
        this.id = id;
        this.specialization = specialization;
        tasksLog=new ArrayList<Task>();
        projectId="none";
        taskId="none";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public ArrayList<Task> getTasksLog() {
        return tasksLog;
    }

    public void setTasksLog(ArrayList<Task> tasksLog) {
        this.tasksLog = tasksLog;
    }
}
