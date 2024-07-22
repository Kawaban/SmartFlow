package api.assignment.domain;


import api.assignment.dto.AssignmentDecision;
import api.assignment.dto.AssignmentResponse;
import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import api.project.ProjectService;
import api.project.domain.Project;
import api.task.TaskService;
import api.task.domain.Task;
import jakarta.persistence.OptimisticLockException;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
record AssignmentService(Algorithm algorithm, ProjectService projectService, AssignmentRepository assignmentRepository,
                         TaskService taskService,
                         DeveloperService developerService) implements api.assignment.AssignmentService {

    public void setAssignment(UUID assignmentId, AssignmentDecision assignmentDecision) throws EntityNotFoundException, OptimisticLockException {
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(EntityNotFoundException::new);

        if (assignmentDecision.isAccepted()) {
            Task task = taskService.findByTaskId(assignment.getTaskId());
            Developer developer = developerService.findByDeveloperId(assignment.getDeveloperId());

            if (task.getAssignedTo() != null) {
                Developer previousDeveloper = task.getAssignedTo();
                previousDeveloper.setTask(null);
                developerService.updateDeveloper(previousDeveloper);
            }

            if (developer.getTask() != null) {
                Task previousTask = developer.getTask();
                previousTask.setAssignedTo(null);
                taskService.updateTask(previousTask);
            }

            task.setAssignedTo(developer);
            developer.setTask(task);
            task.setTaskState(TaskState.ASSIGNED);

            taskService.updateTask(task);
            developerService.updateDeveloper(developer);

        } else {
            assignmentRepository.delete(assignment);
        }

    }


    public ArrayList<AssignmentResponse> delegateTasks(UUID projectId) throws EntityNotFoundException {
        val project = projectService.findByProjectId(projectId);
        ArrayList<Assignment> updateInstances = new ArrayList<Assignment>();

        ArrayList<ArrayList<Task>> tasks = sortTasksBySpecialization(project);
        ArrayList<ArrayList<Developer>> developers = sortDevelopersBySpecialization(project);

        for (int i = 0; i < Specialization.values().length; i++)
            updateInstances.addAll(delegate(tasks.get(i), developers.get(i)));

        ArrayList<AssignmentResponse> assignmentResponses = new ArrayList<>();
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
            tasks.add(new ArrayList<>());

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
            developers.add(new ArrayList<>());

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
