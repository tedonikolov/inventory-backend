package bg.tuvarna.models.dto;

import bg.tuvarna.enums.EmployeePosition;

public record ChangeRoleDTO(String email, EmployeePosition oldRole, EmployeePosition newRole) {
}
