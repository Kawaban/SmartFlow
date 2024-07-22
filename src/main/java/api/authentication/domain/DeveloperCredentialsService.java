package api.authentication.domain;

import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public record DeveloperCredentialsService(DeveloperCredentialsRepository developerCredentialsRepository) {

        public Boolean existsByLogin(String login) {
            return developerCredentialsRepository.existsByUsername(login);
        }

        public DeveloperCredentials findByLogin(String login) {
            return developerCredentialsRepository.findByUsername(login)
                    .orElseThrow(EntityNotFoundException::new);
        }
}
