package api.developer.domain;

import api.developer.dto.DeveloperResponse;
import api.project.domain.Project;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class DeveloperMapper {

        public DeveloperResponse toDeveloperResponse(Developer developer) {
            List<UUID> projects = developer.getProjects().stream()
                    .map(Project::getUuid)
                    .toList();

            return DeveloperResponse.builder()
                    .developerId(developer.getUuid())
                    .specializations(developer.getSpecialization().name())
                    .task(developer.getTask().getUuid())
                    .projects(projects)
                    .build();
        }
}
