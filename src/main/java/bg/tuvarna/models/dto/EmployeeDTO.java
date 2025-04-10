package bg.tuvarna.models.dto;

import bg.tuvarna.enums.EmployeePosition;

public record EmployeeDTO(
        Long id,
        String fullName,
        String username,
        String password,
        String email,
        EmployeePosition employeePosition,
        Long departmentId
) {
}
