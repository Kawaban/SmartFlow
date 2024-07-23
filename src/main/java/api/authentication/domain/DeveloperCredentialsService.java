package api.authentication.domain;

import api.authentication.dto.DeveloperCredentialsRequest;
import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.developer.dto.DeveloperRequest;
import api.infrastructure.exception.EntityNotFoundException;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
record DeveloperCredentialsService(DeveloperCredentialsRepository developerCredentialsRepository, DeveloperService developerService) {

    public Boolean existsByLogin(String login) {
        return developerCredentialsRepository.existsByUsername(login);
    }

    public UserDetails findByLogin(String login) throws EntityNotFoundException {
        return developerCredentialsRepository.findByUsername(login)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void addDeveloperCredentials(DeveloperCredentials developerCredentials) {
        developerCredentialsRepository.save(developerCredentials);
    }

    public void createDeveloper(DeveloperCredentialsRequest developerCredentialsRequest) {
        val developerRequest = DeveloperRequest.builder()
                .login(developerCredentialsRequest.login())
                .specialization(developerCredentialsRequest.specialization())
                .build();

        developerService.addDeveloper(developerRequest);

        val developer = developerService.loadDeveloperByUsername(developerRequest.login());

        addDeveloperCredentials(DeveloperCredentials.builder()
                .username(developerCredentialsRequest.login())
                .password(developerCredentialsRequest.password())
                .role(Role.USER)
                .developer(developer)
                .build());

    }
}
