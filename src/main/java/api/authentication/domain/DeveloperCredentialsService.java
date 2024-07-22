package api.authentication.domain;

import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
record DeveloperCredentialsService(DeveloperCredentialsRepository developerCredentialsRepository) {

    public Boolean existsByLogin(String login) {
        return developerCredentialsRepository.existsByUsername(login);
    }

    public UserDetails findByLogin(String login) throws EntityNotFoundException {
        return developerCredentialsRepository.findByUsername(login)
                .orElseThrow(EntityNotFoundException::new);
    }
}
