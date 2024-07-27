package api.assignment.domain;

import api.assignment.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssignmentServiceTest {

    private final api.assignment.AssignmentService assignmentService;

    @Autowired
    public AssignmentServiceTest(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }



}
