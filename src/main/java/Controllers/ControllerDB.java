package Controllers;

import additionalObjects.FibonacciChecker;
import additionalObjects.Specialization;
import additionalObjects.TaskState;
import algorithm.AlgorithmGreedy;
import baseObjects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

///ControllerDB controls interactions with PostgreSQL DB via Hibernate framework, it can get objects to controllerIO by request or save/update objects taken from controllerIO,
// also creates necessary for correct work objects
public class ControllerDB {
    private final SessionFactory sessionFactory;

    public ControllerDB() {

        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        cfg.addAnnotatedClass(Developer.class);
        cfg.addAnnotatedClass(Task.class);
        cfg.addAnnotatedClass(Project.class);
        cfg.addAnnotatedClass(TaskLog.class);
        cfg.addAnnotatedClass(Assignment.class);
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
        sb.applySettings(cfg.getProperties());
        StandardServiceRegistry standardServiceRegistry = sb.build();
        SessionFactory sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
        this.sessionFactory = sessionFactory;
    }

    public String findProjectById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Project i WHERE i.id=" + id, Project.class);
        String projectJSONString = null;
        if (!query.getResultList().isEmpty()) {
            Project project = (Project) query.getSingleResult();
            projectJSONString = project.ToJSONObject(true).toString();
        }
        session.getTransaction().commit();
        return projectJSONString;

    }


    public String findDeveloperById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Developer i WHERE i.id=" + id, Developer.class);
        String developerJSONString = null;
        if (!query.getResultList().isEmpty()) {
            Developer developer = (Developer) query.getSingleResult();
            developerJSONString = developer.ToJSONObject(true).toString();
        }
        session.getTransaction().commit();
        return developerJSONString;
    }

    public String findTaskById(long projectId, long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Task i WHERE i.id=" + id, Task.class);
        String taskJSONString = null;
        if (!query.getResultList().isEmpty()) {
            Task task = (Task) query.getSingleResult();
            taskJSONString = task.ToJSONObject().toString();
        }
        session.getTransaction().commit();
        return taskJSONString;
    }

    public void createNewProject(JSONObject projectJSON) {
        int projectIntID = (int) projectJSON.opt("id");
        long projectId = projectIntID;

        if (!checkProjectId(projectId))
            throw new RuntimeException("Error, project with this id already exists");

        String projectName = (String) projectJSON.opt("name");

        JSONArray developersJSON = projectJSON.optJSONArray("developers");
        ArrayList<Developer> developers = new ArrayList<Developer>();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        for (int i = 0; i < developersJSON.length(); i++) {
            int developerIntID = (int) developersJSON.opt(i);
            long developerId = developerIntID;
            Query query = session.createQuery("SELECT i FROM Developer i WHERE i.id=" + developerId, Developer.class);
            if (!query.getResultList().isEmpty()) {
                Developer developer = (Developer) query.getSingleResult();
                developers.add(developer);
            }
        }
        Project project = new Project(projectId, projectName, developers);

        session.save(project);
        session.getTransaction().commit();

    }

    public void createNewDeveloper(JSONObject developerJSON) {
        int developerIntID = (int) developerJSON.opt("id");
        long developerId = developerIntID;

        if (!checkDeveloperId(developerId))
            throw new RuntimeException("Error, developer with this id already exists");

        String developerSpecStr = (String) developerJSON.opt("specialization");
        Specialization developerSpec;
        try {
            developerSpec = Specialization.valueOf(developerSpecStr);
        } catch (RuntimeException e) {

            throw new RuntimeException("Error, Wrong specialization");
        }


        Developer developer = new Developer(developerId, developerSpec);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(developer);
        session.getTransaction().commit();
    }

    public void createNewTask(JSONObject taskJSON, long projectId) {


        int taskIntID = (int) taskJSON.opt("id");
        long taskId = taskIntID;
        if (!checkTaskId(taskId)) {
            throw new RuntimeException("Error, task with this id already exists");
        }

        String name = taskJSON.optString("name");

        int creatorIntID = (int) taskJSON.opt("createdBy");
        long creatorId = creatorIntID;

        String taskSpecStr = (String) taskJSON.opt("specialization");
        Specialization taskSpec;
        try {
            taskSpec = Specialization.valueOf(taskSpecStr);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error, wrong specialization");
        }

        int estimation = (int) taskJSON.opt("estimation");
        if (!FibonacciChecker.isFibonacci(estimation)) {

            throw new RuntimeException("estimation is not fibonacci number");
        }

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT i FROM Project i WHERE i.id=" + projectId, Project.class);
        Project project;
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            throw new RuntimeException("Error, wrong project id");
        }
        project = (Project) query.getSingleResult();


        Integer developerIntID = (Integer) taskJSON.opt("assignedTo");
        Developer developer = null;
        if (developerIntID != null) {
            long developerId = developerIntID;
            Query query1 = session.createQuery("SELECT i FROM Developer i WHERE i.id=" + developerId, Developer.class);
            if (query1.getResultList().isEmpty()) {
                session.getTransaction().commit();
                throw new RuntimeException("wrong developer id");
            }
            developer = (Developer) query1.getSingleResult();
        }

        String createdAtStr = taskJSON.getString("createdAt");
        LocalDate createdAt;
        try {
            createdAt = LocalDate.parse(createdAtStr);
        } catch (RuntimeException e) {
            session.getTransaction().commit();
            throw new RuntimeException("Wrong createdAt Date");
        }


        String deadlineStr = taskJSON.getString("deadline");
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(deadlineStr);
        } catch (RuntimeException e) {
            session.getTransaction().commit();
            throw new RuntimeException("Wrong deadline Date");
        }

        Task task;
        if (developer == null)
            task = new Task(taskId, project, createdAt, deadline, creatorId, name, estimation, taskSpec);
        else {
            task = new Task(taskId, project, createdAt, deadline, creatorId, name, estimation, taskSpec, developer);
            developer.setTask(task);
            session.update(developer);
        }
        session.save(task);
        session.getTransaction().commit();

    }

    public void EditTask(long projectId, long taskId, JSONObject taskJSON) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Task i WHERE i.id=" + taskId, Task.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            throw new RuntimeException("Object wasn't found");
        }
        Task task = (Task) query.getSingleResult();

        if (task.getProject().getId() != projectId) {
            session.getTransaction().commit();
            throw new RuntimeException("Wrong project id");
        }

        if (task.getTaskState() == TaskState.FAILED || task.getTaskState() == TaskState.COMPLETED || task.getTaskState() == TaskState.SKIPPED) {
            session.getTransaction().commit();
            throw new RuntimeException("Impossible to change status of this task");
        }

        String taskStateStr = taskJSON.optString("taskState");
        TaskState taskState;
        try {
            taskState = TaskState.valueOf(taskStateStr);
        } catch (RuntimeException e) {
            session.getTransaction().commit();
            throw new RuntimeException("Wrong TaskState");
        }

        if ((taskState == TaskState.FAILED || taskState == TaskState.COMPLETED || taskState == TaskState.SKIPPED) && task.getAssignedTo() != null) {
            createTaskLog(task, task.getAssignedTo(), session, taskState);
            Developer developer;
            developer = task.getAssignedTo();
            developer.setTask(null);
            session.update(developer);
            task.setAssignedTo(null);


        }


        task.setTaskState(taskState);
        session.update(task);
        session.getTransaction().commit();
    }

    public void createTaskLog(Task task, Developer developer, Session session, TaskState taskState) {
        TaskLog taskLog = new TaskLog(task.getId(), task.getProject().getId(), developer, task.getDeadline(), LocalDate.now(), task.getCreatedAt(), taskState, task.getEstimation());
        session.save(taskLog);
    }


    public ArrayList<JSONObject> createAssignments(long projectId, JSONObject assignmentJSON) {
        int assignmentInt = (int) assignmentJSON.opt("id");
        long assignmentID = assignmentInt;

        if (!checkAssignmentId(assignmentID)) {
            throw new RuntimeException("Error, assignment with this id already exists");
        }

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Project i WHERE i.id=" + projectId, Project.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            throw new RuntimeException("Error, wrong project id");
        }
        Project project = (Project) query.getSingleResult();

        TaskDelegator taskDelegator = new TaskDelegator(new AlgorithmGreedy());
        ArrayList<Assignment> updateInstances = taskDelegator.delegateTasks(project);

        JSONArray assignmentsJSONArray = new JSONArray();
        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();

        for (Assignment assignment : updateInstances) {
            assignment.setId(assignmentID);
            assignment.setProjectId(projectId);
            session.save(assignment);
            JSONObject object = assignment.ToJSONObject();
            objects.add(object);
        }

        session.getTransaction().commit();
        return objects;
    }

    public void decideDelegationOfTasks(long assignmentId, long projectId, JSONObject decision) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Assignment i WHERE i.id=" + assignmentId, Assignment.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            throw new RuntimeException("Error wrong assignment id");
        }
        List<Assignment> assignments = query.getResultList();
        if (assignments.get(0).getProjectId() != projectId) {
            session.getTransaction().commit();
            throw new RuntimeException("Error wrong project id");
        }

        if (decision.optString("decision").equals("Y")) {
            for (Assignment assignment : assignments) {
                Task task = session.createQuery("SELECT i FROM Task i WHERE i.id=" + assignment.getTaskId(), Task.class)
                        .getSingleResult();
                Developer developer = session.createQuery("SELECT i FROM Developer i WHERE i.id=" + assignment.getDeveloperId(), Developer.class)
                        .getSingleResult();
                developer.setTask(task);
                task.setAssignedTo(developer);
                task.setTaskState(TaskState.ASSIGNED);
                session.update(developer);
                session.update(task);
                session.remove(assignment);
            }
            session.getTransaction().commit();
        } else if (decision.optString("decision").equals("N")) {
            for (Assignment assignment : assignments) {
                session.remove(assignment);
            }
            session.getTransaction().commit();
        } else {
            session.getTransaction().commit();
            throw new RuntimeException("Error wrong decision input");
        }
    }


    public Boolean checkTaskId(long taskId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Task i WHERE i.id=" + taskId, Task.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            return true;
        }
        session.getTransaction().commit();
        return false;

    }

    public Boolean checkProjectId(long projectId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Project i WHERE i.id=" + projectId, Project.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            return true;
        }
        session.getTransaction().commit();
        return false;
    }

    public Boolean checkDeveloperId(long developerId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Developer i WHERE i.id=" + developerId, Developer.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            return true;
        }
        session.getTransaction().commit();
        return false;
    }

    public Boolean checkAssignmentId(long assignmentId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT i FROM Assignment i WHERE i.id=" + assignmentId, Assignment.class);
        if (query.getResultList().isEmpty()) {
            session.getTransaction().commit();
            return true;
        }
        session.getTransaction().commit();
        return false;
    }


}
