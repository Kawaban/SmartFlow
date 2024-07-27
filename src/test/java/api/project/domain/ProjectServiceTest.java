package api.project.domain;

import api.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectServiceTest {

        private final api.project.ProjectService projectService;

        @Autowired
        public ProjectServiceTest(ProjectService projectService) {
            this.projectService = projectService;
        }
}
