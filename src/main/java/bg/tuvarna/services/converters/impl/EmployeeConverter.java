package bg.tuvarna.services.converters.impl;

import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.services.converters.Converter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {
    @Override
    public EmployeeDTO convertToDto(Employee entity) {
        return new EmployeeDTO(entity.id,
                entity.getFullName(),
                entity.getUsername(),
                entity.getEmail(),
                null,
                entity.getPosition(),
                entity.getDepartment().id
        );
    }

    @Override
    public Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setFullName(dto.fullName());
        employee.setUsername(dto.username());
        employee.setEmail(dto.email());
        employee.setPosition(dto.employeePosition());

        return employee;
    }

    @Override
    public Employee updateEntity(Employee entity, EmployeeDTO dto) {
        entity.setFullName(dto.fullName());
        entity.setPosition(dto.employeePosition());

        return entity;
    }
}
