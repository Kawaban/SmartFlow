package Controllers;

import baseObjects.Developer;
import baseObjects.Project;
import baseObjects.Task;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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



}
