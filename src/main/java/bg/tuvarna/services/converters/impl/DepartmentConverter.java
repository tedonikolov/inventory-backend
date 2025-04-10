package bg.tuvarna.services.converters.impl;

import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.models.entities.Department;
import bg.tuvarna.services.converters.Converter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DepartmentConverter implements Converter<DepartmentDTO, Department> {

    @Override
    public DepartmentDTO convertToDto(Department entity) {
       return new DepartmentDTO(entity.id,
               entity.getName(),
               entity.getDescription()
       );
    }

    @Override
    public Department convertToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setName(dto.name());
        department.setDescription(dto.description());

        return department;
    }

    @Override
    public Department updateEntity(Department entity, DepartmentDTO dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());

        return entity;
    }
}
