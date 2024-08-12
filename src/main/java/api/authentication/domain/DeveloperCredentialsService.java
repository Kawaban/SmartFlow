package api.authentication.domain;

import api.authentication.dto.DeveloperCredentialsRequest;
import api.developer.DeveloperService;
import api.developer.dto.DeveloperRequest;
import api.infrastructure.exception.EntityNotFoundException;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
record DeveloperCredentialsService(DeveloperCredentialsRepository developerCredentialsRepository, DeveloperService developerService) implements UserDetailsService {

    public Boolean existsByLogin(String login) {
        return developerCredentialsRepository.existsByUsername(login);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return developerCredentialsRepository.findByUsername(username)
                .orElseThrow(EntityNotFoundException::new);
    }
}
