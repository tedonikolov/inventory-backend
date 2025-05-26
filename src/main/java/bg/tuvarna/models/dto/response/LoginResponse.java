package bg.tuvarna.models.dto.response;

public record LoginResponse(
        String token,
        String refreshToken
) {
}
