package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.models.entities.Department;
import bg.tuvarna.reporsitory.DepartmentRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.DepartmentService;
import bg.tuvarna.services.converters.impl.DepartmentConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentConverter converter;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentConverter converter) {
        repository = departmentRepository;
        this.converter = converter;
    }

    @Override
    @Transactional
    public void save(DepartmentDTO department) {
        if (findDepartmentByName(department.name()) != null) {
            throw new CustomException("Department already exists", ErrorCode.AlreadyExists);
        }

        if (department.id() != null) {
            Department entity = findDepartmentById(department.id());
            repository.persist(converter.updateEntity(entity, department));
        } else
            repository.persist(converter.convertToEntity(department));
    }

    @Override
    @Transactional
    public void removeDepartment(Long id) {
        repository.delete(findDepartmentById(id));
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return repository.findAll().stream().map(converter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartment(Long id) {
        return converter.convertToDto(findDepartmentById(id));
    }

    @Override
    public Department findDepartmentById(Long id) {
        return repository.findByIdOptional(id).orElseThrow(() ->
                new CustomException("Department not found", ErrorCode.EntityNotFound));
    }

    @Override
    public Department findDepartmentByName(String departmentName) {
        return repository.find("LOWER(name)", departmentName.toLowerCase()).firstResult();
    }
}
