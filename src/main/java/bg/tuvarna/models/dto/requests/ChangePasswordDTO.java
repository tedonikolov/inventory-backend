package bg.tuvarna.models.dto.requests;

public record ChangePasswordDTO(
    String username,
    String password
) {
}
