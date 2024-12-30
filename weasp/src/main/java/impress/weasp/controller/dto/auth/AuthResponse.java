package impress.weasp.controller.dto.auth;


public record AuthResponse(
        String token,
        String message,
        String role
) {}



