package api.authentication;

import api.authentication.dto.AuthRequest;
import api.authentication.dto.AuthResponse;
import api.authentication.dto.DeveloperCredentialsRequest;
import api.infrastructure.exception.DeveloperAlreadyExistsException;
import api.infrastructure.exception.EntityNotFoundException;

public interface AuthenticateService {
    AuthResponse login(AuthRequest request) throws EntityNotFoundException;

    void register(DeveloperCredentialsRequest developerCredentialsRequest) throws DeveloperAlreadyExistsException, EntityNotFoundException;
}
