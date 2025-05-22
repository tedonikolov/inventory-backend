package bg.tuvarna.models.dto.requests;

public record EmployeePhoneTokenDTO(
        Long employeeId,
        String phoneToken
) {
}