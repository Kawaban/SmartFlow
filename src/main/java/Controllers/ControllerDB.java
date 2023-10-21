package Controllers;

import additionalObjects.Specialization;
import baseObjects.Developer;
import baseObjects.Project;
import baseObjects.Task;
import baseObjects.TaskLog;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ControllerDB {
    private SessionFactory sessionFactory;

    public ControllerDB() {
        final SessionFactory sessionFactory=new Configuration()
                .addAnnotatedClass(Developer.class)
                .addAnnotatedClass(Project.class)
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(TaskLog.class)
                .buildSessionFactory();
        this.sessionFactory=sessionFactory;
    }

    public String findProjectById(long id)
    {
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        Project project=session.createQuery("SELECT i FROM Project i WHERE i.id="+id,Project.class)
                .getSingleResult();
        String projectJSONString=project.ToJSONObject().toString();
        session.getTransaction().commit();
        return projectJSONString;

    }

    public String findDeveloperById(long id)
    {
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        Developer developer=session.createQuery("SELECT i FROM Developer i WHERE i.id="+id,Developer.class)
                .getSingleResult();
        String developerJSONString=developer.ToJSONObject(true).toString();
        session.getTransaction().commit();
        return developerJSONString;
    }

    public String findTaskById(long projectId,long id)
    {
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        Task task=session.createQuery("SELECT i FROM Task i WHERE i.id="+id,Task.class)
                        .getSingleResult();
        String taskJSONString=null;
        if(task.getId()==projectId)
             taskJSONString=task.ToJSONObject().toString();
        session.getTransaction().commit();
        return taskJSONString;
    }

    public void createNewProject(JSONObject projectJSON)
    {
        int projectIntID= (int) projectJSON.opt("id");
        long projectId= projectIntID;

        String projectName= (String) projectJSON.opt("name");

        JSONArray developersJSON=projectJSON.optJSONArray("developers");
        ArrayList<Developer> developers=new ArrayList<Developer>();
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        for(int i=0;i<developersJSON.length();i++) {
            int developerIntID= (int) projectJSON.opt("id");
            long developerId= developerIntID;
            Developer developer=session.createQuery("SELECT i FROM Developer i WHERE i.id="+developerId,Developer.class)
                    .getSingleResult();
            if(developer!=null)
                developers.add(developer);
        }
        Project project=new Project(projectId,projectName,developers);
        session.save(project);
        session.getTransaction().commit();

    }

    public void createNewDeveloper(JSONObject developerJSON)
    {
        int developerIntID= (int) developerJSON.opt("id");
        long developerId= developerIntID;

        String developerSpecStr= (String) developerJSON.opt("specialization");
        Specialization developerSpec=Specialization.valueOf(developerSpecStr);


        Developer developer=new Developer(developerId,developerSpec);

        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(developer);
        session.getTransaction().commit();
    }

    public Boolean createNewTask(JSONObject taskJSON, long projectId)
    {
        return false;
    }

    public Boolean EditNewTask(long projectId, long taskId, JSONObject taskJSON)
    {
        return  false;
    }




}
