package api.authentication.domain;

import api.authentication.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticateServiceTest {

        private final api.authentication.AuthenticateService authenticateService;

        @Autowired
        public AuthenticateServiceTest(AuthenticateService authenticateService) {
            this.authenticateService = authenticateService;
        }

}
