package api.authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Arrays;

@Builder
public record DeveloperCredentialsRequest(@NotEmpty String login, @NotNull char[] password, @NotNull String specialization) {
    public void zeroPassword(){
        Arrays.fill(password, (char) 0);
    }
    public String getPasswordAsString(){return new String(password);}
}
