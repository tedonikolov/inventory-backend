package bg.tuvarna.services.converters.impl;

import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.services.converters.Converter;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EmployeeConverter implements Converter<EmployeeDTO, Employee> {
    @ConfigProperty(name = "employee.image.path")
    String pathToImage;

    @Override
    public EmployeeDTO convertToDto(Employee entity) {
        return new EmployeeDTO(entity.id,
                entity.getFullName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPosition(),
                entity.getDepartment().id,
                entity.getImageUrl() != null ? pathToImage + entity.getImageUrl() : null
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
