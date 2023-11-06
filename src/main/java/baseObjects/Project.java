package baseObjects;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="projects")
public class Project {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(
            name="project_developers",
            joinColumns=@JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="developer_id")
    )
    List<Developer> projectDevelopers;
    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY)
    List<Task> tasks;

    public Project(long id, String name, ArrayList<Developer> projectDevelopers) {
        this.id = id;
        this.name = name;
        this.projectDevelopers = projectDevelopers;
    }

    public Project() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Developer> getProjectDevelopers() {
        return projectDevelopers;
    }

    public void setProjectDevelopers(ArrayList<Developer> projectDevelopers) {
        this.projectDevelopers = projectDevelopers;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public JSONObject ToJSONObject(Boolean extendedInfo)
    {
        JSONObject obj=new JSONObject();
        obj.put("id",id);
        obj.put("name",name);

        if(extendedInfo) {
            ArrayList<JSONObject> developers = new ArrayList<JSONObject>();
            if (projectDevelopers != null)
                for (Developer developer : projectDevelopers)
                    developers.add(developer.ToJSONObject(false));

            obj.put("developers", developers);
            ArrayList<JSONObject> tasksJSON = new ArrayList<JSONObject>();
            System.out.println(-1);
            if (tasks != null)
                for (Task task : tasks)
                    tasksJSON.add(task.ToJSONObject());
            obj.put("tasks", tasksJSON);
        }
        return obj;
    }
}
