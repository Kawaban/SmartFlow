package Controllers;

import additionalObjects.Specialization;
import additionalObjects.TaskState;
import baseObjects.Developer;
import baseObjects.Project;
import baseObjects.Task;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class TaskDelegator {
    public static void delegateTasks(Project project)
    {

        ArrayList<ArrayList<Task>> tasks = sortTasksBySpecialization(project);
        ArrayList<ArrayList<Developer>> developers = sortDevelopersBySpecialization(project);

        for(int i=0;i<Specialization.values().length;i++)
            delegate(tasks.get(i),developers.get(i));

    }

    private static ArrayList<ArrayList<Task>> sortTasksBySpecialization(Project project)
    {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
        for(int i=0;i< Specialization.values().length;i++)
            tasks.add(new ArrayList<Task>());

        for(Task task:project.getTasks()) {
            if (task.getAssignedTo() == "none") {
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

    private static ArrayList<ArrayList<Developer>> sortDevelopersBySpecialization(Project project)
    {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Developer>> developers = new ArrayList<ArrayList<Developer>>();
        for(int i=0;i< Specialization.values().length;i++)
            developers.add(new ArrayList<Developer>());

        for(Developer developer:project.getProjectDevelopers()) {
            if (developer.getTaskId() == "none") {
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

    private static void delegate(ArrayList<Task> tasks, ArrayList<Developer> developers)
    {
        Map<Integer,ArrayList<Task>> taskMap=new HashMap<Integer,ArrayList<Task>>();
        Map<Integer, Queue<Developer>> developerMap=new HashMap<Integer, Queue<Developer>>();
        for(Task task:tasks) {
            if (taskMap.get(task.getEstimation()) == null) {
                taskMap.put(task.getEstimation(), new ArrayList<Task>());
            }
            taskMap.get(task.getEstimation()).add(task);
        }




    }


}
