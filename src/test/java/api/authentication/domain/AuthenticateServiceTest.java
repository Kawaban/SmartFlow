package api.authentication.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import api.authentication.dto.AuthRequest;
import api.authentication.dto.AuthResponse;
import api.authentication.dto.DeveloperCredentialsRequest;
import api.developer.domain.Developer;
import api.infrastructure.exception.DeveloperAlreadyExistsException;
import api.infrastructure.exception.EntityNotFoundException;
import api.jwt.JwtService;
import api.jwt.dto.JwtUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AuthenticateServiceTest {

    @Mock
    private DeveloperCredentialsService developerCredentialsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticateService authenticateService;

    @Test
    void testLogin_ValidCredentials() throws EntityNotFoundException {
        String login = "testuser";
        String password = "password";
        String token = "jwtToken";

        AuthRequest request = new AuthRequest(login, password.toCharArray());

        when(developerCredentialsService.loadUserByUsername(login)).thenReturn(new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public String getPassword() {
                return "password";
            }

            @Override
            public String getUsername() {
                return "testuser";
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }
        });
        when(jwtService.generateToken(any(JwtUser.class))).thenReturn(token);

        AuthResponse response = authenticateService.login(request);

        assertNotNull(response);
        assertEquals(token, response.token());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any(JwtUser.class));
    }

    @Test
    void testLogin_InvalidCredentials() {
        String login = "testuser";
        String password = "wrongpassword";

        AuthRequest request = new AuthRequest(login, password.toCharArray());

        when(developerCredentialsService.loadUserByUsername(login)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> authenticateService.login(request));

        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any(JwtUser.class));
    }

    @Test
    void testRegister_NewDeveloper() throws DeveloperAlreadyExistsException, EntityNotFoundException {
        String login = "newuser";
        DeveloperCredentialsRequest request = new DeveloperCredentialsRequest(login, "password".toCharArray(),"BACKEND");

        when(developerCredentialsService.existsByLogin(login)).thenReturn(false);

        authenticateService.register(request);

        verify(developerCredentialsService, times(1)).createDeveloper(any(DeveloperCredentialsRequest.class));
    }

    @Test
    void testRegister_ExistingDeveloper() {
        String login = "existinguser";
        DeveloperCredentialsRequest request = new DeveloperCredentialsRequest(login, "password".toCharArray(),"BACKEND");

        when(developerCredentialsService.existsByLogin(login)).thenReturn(true);

        assertThrows(DeveloperAlreadyExistsException.class, () -> authenticateService.register(request));

        verify(developerCredentialsService, never()).createDeveloper(any(DeveloperCredentialsRequest.class));
    }
}