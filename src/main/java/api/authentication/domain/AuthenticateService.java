package api.authentication.domain;

import api.authentication.dto.AuthRequest;
import api.authentication.dto.AuthResponse;
import api.authentication.dto.DeveloperCredentialsRequest;
import api.developer.domain.DeveloperService;
import api.developer.dto.DeveloperRequest;
import api.infrastructure.exception.DeveloperAlreadyExistsException;
import api.jwt.JwtService;
import api.jwt.dto.JwtUser;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record AuthenticateService(DeveloperCredentialsService developerCredentialsService, DeveloperService developerService, AuthenticationManager authenticationManager, JwtService jwtService) {

        public AuthResponse login(AuthRequest request) {
                val developer = developerCredentialsService.findByLogin(request.login());
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.login(), request.password(), List.of()));
                String token = jwtService().generateToken(new JwtUser(request.login()));
                request.zeroPassword();
                return new AuthResponse(token);
        }

        public void register(DeveloperCredentialsRequest developerCredentialsRequest) {
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
