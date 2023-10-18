package baseObjects;

import java.util.ArrayList;

public class Project {
    private String _id;
    private String name;
    ArrayList<Developer> projectDevelopers;
    ArrayList<Task> tasks;

    public Project(String _id, String name, ArrayList<Developer> projectDevelopers) {
        this._id = _id;
        this.name = name;
        this.projectDevelopers = projectDevelopers;
        tasks=new ArrayList<Task>();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Developer> getProjectDevelopers() {
        return projectDevelopers;
    }

    public void setProjectDevelopers(ArrayList<Developer> projectDevelopers) {
        this.projectDevelopers = projectDevelopers;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
