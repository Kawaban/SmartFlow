package api.jwt.domain;

import api.jwt.JwtService;
import api.jwt.dto.JwtUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class JwtServiceTest {

    private final api.jwt.JwtService jwtService;

    @Autowired
    public JwtServiceTest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Test
    public void extractUsernameTest() {
        String username = "username123";
        JwtUser user = new JwtUser(username);

        String token = jwtService.generateToken(user);
        assertEquals(jwtService.extractUsername(token), username);
    }

    @Test
    public void extractEmptyUsernameTest() {
        String username = "";
        JwtUser user = new JwtUser(username);

        String token = jwtService.generateToken(user);
        assertEquals(jwtService.extractUsername(token), username);
    }

    @Test
    public void extractNullUsernameTest() {
        String token = jwtService.generateToken(null);
        assertNull(jwtService.extractUsername(token));
    }

    @Test
    public void validTokenTest() {
        String username = "username123";
        JwtUser user = new JwtUser(username);

        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    public void invalidTokenTest() {
        String username1 = "username123";
        JwtUser user1 = new JwtUser(username1);

        String username2 = "admin";
        JwtUser user2 = new JwtUser(username2);

        String token = jwtService.generateToken(user1);
        assertFalse(jwtService.isTokenValid(token, user2));
    }
}
