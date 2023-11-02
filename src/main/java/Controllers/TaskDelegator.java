package Controllers;

import additionalObjects.Rank;
import additionalObjects.Specialization;
import additionalObjects.TaskState;
import algorithm.Algorithm;
import baseObjects.Assignment;
import baseObjects.Developer;
import baseObjects.Project;
import baseObjects.Task;

import java.util.*;

public class TaskDelegator {
    protected Algorithm algorithm;

    public TaskDelegator(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public  ArrayList<Assignment> delegateTasks(Project project)
    {
        ArrayList<Assignment> updateInstances=new ArrayList<Assignment>();

        ArrayList<ArrayList<Task>> tasks = sortTasksBySpecialization(project);
        ArrayList<ArrayList<Developer>> developers = sortDevelopersBySpecialization(project);

        for(int i=0;i<Specialization.values().length;i++)
            updateInstances.addAll(delegate(tasks.get(i),developers.get(i)));

        return updateInstances;
    }

    private  ArrayList<ArrayList<Task>> sortTasksBySpecialization(Project project)
    {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
        for(int i=0;i< Specialization.values().length;i++)
            tasks.add(new ArrayList<Task>());

        for(Task task:project.getTasks()) {
            if (task.getAssignedTo() == null && task.getTaskState()== TaskState.DEFAULT) {
                switch (task.getSpecialization()) {
                    case FRONTEND:
                        tasks.get(0).add(task);
                        break;
                    case BACKEND:
                        tasks.get(1).add(task);
                        break;
                    case DEVOPS:
                        tasks.get(2).add(task);
                        break;
                    case UX_UI:
                        tasks.get(3).add(task);
                        break;
                }
            }
        }
        return tasks;
    }

    private  ArrayList<ArrayList<Developer>> sortDevelopersBySpecialization(Project project)
    {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Developer>> developers = new ArrayList<ArrayList<Developer>>();
        for(int i=0;i< Specialization.values().length;i++)
            developers.add(new ArrayList<Developer>());

        for(Developer developer:project.getProjectDevelopers()) {
            if (developer.getTask() == null) {
                switch (developer.getSpecialization()) {
                    case FRONTEND:
                        developers.get(0).add(developer);
                        break;
                    case BACKEND:
                        developers.get(1).add(developer);
                        break;
                    case DEVOPS:
                        developers.get(2).add(developer);
                        break;
                    case UX_UI:
                        developers.get(3).add(developer);
                        break;
                }
            }
        }
        return developers;
    }

    private  ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers)
    {

        ArrayList<Assignment> updateInstances=algorithm.delegate(tasks,developers);

        return updateInstances;
    }


}
