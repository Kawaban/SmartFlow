package api.authentication.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String token) {
}
