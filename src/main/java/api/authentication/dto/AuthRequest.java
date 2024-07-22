package api.authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;

public record AuthRequest(@NotEmpty String login, @NotNull char[] password) {
    public void zeroPassword(){
        Arrays.fill(password, (char) 0);
    }

    public String getPasswordAsString(){
        return new String(password);
    }
}
