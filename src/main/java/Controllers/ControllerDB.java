package Controllers;

import baseObjects.Developer;
import baseObjects.Project;
import baseObjects.Task;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

public class ControllerDB {
    private SessionFactory sessionFactory;

    public ControllerDB() {
        final SessionFactory sessionFactory=new Configuration()
                .addAnnotatedClass(Developer.class)
                .addAnnotatedClass(Project.class)
                .addAnnotatedClass(Task.class)
                .buildSessionFactory();
        this.sessionFactory=sessionFactory;
    }

    public String findProjectById(String id)
    {
        return null;
    }

    public String findDeveloperById(String id)
    {
        return null;
    }

    public String findTaskById(String projectId,String id)
    {
        return null;
    }

    public void createNewProject(JSONObject projectJSON)
    {

    }

    public void createNewDeveloper(JSONObject developerJSON)
    {

    }

    public Boolean createNewTask(JSONObject taskJSON, String projectId)
    {
        return false;
    }

    public Boolean EditNewTask(String projectId, String taskId, JSONObject taskJSON)
    {
        return  false;
    }




}
