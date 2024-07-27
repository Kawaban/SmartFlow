package api.developer.domain;

import api.developer.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeveloperServiceTest {
    private final api.developer.DeveloperService developerService;

    @Autowired
    public DeveloperServiceTest(DeveloperService developerService) {
        this.developerService = developerService;
    }
}
