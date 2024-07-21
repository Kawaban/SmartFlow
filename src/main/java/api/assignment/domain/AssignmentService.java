package api.assignment.domain;




import api.developer.domain.DeveloperRepository;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.domain.Project;
import api.project.domain.ProjectRepository;
import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;
import api.task.domain.Task;
import api.task.domain.TaskRepository;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
record AssignmentService(Algorithm algorithm, ProjectRepository projectRepository, AssignmentRepository assignmentRepository, TaskRepository taskRepository, DeveloperRepository developerRepository) implements api.assignment.AssignmentService {

    public void setAssignment(UUID assignmentId, AssignmentDecision assignmentDecision) {
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(EntityNotFoundException::new);

        if (assignmentDecision.isAccepted()) {
            Task task = taskRepository.findById(assignment.getTaskId()).orElseThrow(EntityNotFoundException::new);
            Developer developer = developerRepository.findById(assignment.getDeveloperId()).orElseThrow(EntityNotFoundException::new);

            if(task.getAssignedTo() != null) {
                Developer previousDeveloper = task.getAssignedTo();
                previousDeveloper.setTask(null);
                developerRepository.save(previousDeveloper);
            }

            if (developer.getTask() != null) {
                Task previousTask = developer.getTask();
                previousTask.setAssignedTo(null);
                taskRepository.save(previousTask);
            }

            task.setAssignedTo(developer);
            developer.setTask(task);
            task.setTaskState(TaskState.ASSIGNED);

            taskRepository.save(task);
            developerRepository.save(developer);

        } else {
            assignmentRepository.delete(assignment);
        }


    }


    public ArrayList<AssignmentResponse> delegateTasks(UUID projectId) {
        val project = projectRepository.findByProjectId(projectId).orElseThrow(EntityNotFoundException::new);
        ArrayList<Assignment> updateInstances = new ArrayList<Assignment>();

        ArrayList<ArrayList<Task>> tasks = sortTasksBySpecialization(project);
        ArrayList<ArrayList<Developer>> developers = sortDevelopersBySpecialization(project);

        for (int i = 0; i < Specialization.values().length; i++)
            updateInstances.addAll(delegate(tasks.get(i), developers.get(i)));

        ArrayList<AssignmentResponse> assignmentResponses =new ArrayList<>();
        for (Assignment assignment : updateInstances) {
            assignmentResponses.add(AssignmentResponse.builder()
                                                      .taskId(assignment.getTaskId())
                                                      .developerId(assignment.getDeveloperId())
                                                      .assignmentId(assignment.getUuid())
                                                      .build());
        }

        return assignmentResponses;
    }

    private ArrayList<ArrayList<Task>> sortTasksBySpecialization(Project project) {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Task>> tasks = new ArrayList<ArrayList<Task>>();
        for (int i = 0; i < Specialization.values().length; i++)
            tasks.add(new ArrayList<Task>());

        for (Task task : project.getTasks()) {
            if (task.getAssignedTo() == null && task.getTaskState() == TaskState.DEFAULT) {
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

    private ArrayList<ArrayList<Developer>> sortDevelopersBySpecialization(Project project) {
        ///0 FRONTEND
        ///1 BACKEND
        ///2 DEVOPS
        ///3 UX/UI
        ArrayList<ArrayList<Developer>> developers = new ArrayList<ArrayList<Developer>>();
        for (int i = 0; i < Specialization.values().length; i++)
            developers.add(new ArrayList<Developer>());

        for (Developer developer : project.getProjectDevelopers()) {
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

    private ArrayList<Assignment> delegate(ArrayList<Task> tasks, ArrayList<Developer> developers) {
        return algorithm.delegate(tasks, developers);
    }


}
