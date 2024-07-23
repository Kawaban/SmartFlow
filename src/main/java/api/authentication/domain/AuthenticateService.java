package api.authentication.domain;

import api.authentication.dto.AuthRequest;
import api.authentication.dto.AuthResponse;
import api.authentication.dto.DeveloperCredentialsRequest;
import api.developer.DeveloperService;
import api.developer.dto.DeveloperRequest;
import api.infrastructure.exception.DeveloperAlreadyExistsException;
import api.infrastructure.exception.EntityNotFoundException;
import api.jwt.JwtService;
import api.jwt.dto.JwtUser;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
record AuthenticateService(DeveloperCredentialsService developerCredentialsService, DeveloperService developerService,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) implements api.authentication.AuthenticateService {

    public AuthResponse login(AuthRequest request) throws EntityNotFoundException {
        val developer = developerCredentialsService.findByLogin(request.login());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.login(), request.password(), developer.getAuthorities()));
        String token = jwtService().generateToken(new JwtUser(developer.getUsername()));
        request.zeroPassword();
        return new AuthResponse(token);
    }

    public void register(DeveloperCredentialsRequest developerCredentialsRequest) throws DeveloperAlreadyExistsException, EntityNotFoundException {
        if (developerCredentialsService.existsByLogin(developerCredentialsRequest.login())) {
            throw new DeveloperAlreadyExistsException();
        }



        val developerRequest = DeveloperRequest.builder()
                .login(developerCredentialsRequest.login())
                .specialization(developerCredentialsRequest.specialization())
                .build();

        developerService.addDeveloper(developerRequest);
    }
}
