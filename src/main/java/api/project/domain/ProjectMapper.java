package api.project.domain;

import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class ProjectMapper {

        public ProjectResponse toProjectResponse(Project project) {
            List<UUID> tasks = new ArrayList<>();
            project.getTasks().forEach(task -> tasks.add(task.getUuid()));

            List<UUID> developers = new ArrayList<>();
            project.getProjectDevelopers().forEach(developer -> developers.add(developer.getUuid()));

            return ProjectResponse.builder()
                    .projectId(project.getUuid())
                    .projectName(project.getName())
                    .tasks(tasks)
                    .developers(developers)
                    .build();
        }

}
