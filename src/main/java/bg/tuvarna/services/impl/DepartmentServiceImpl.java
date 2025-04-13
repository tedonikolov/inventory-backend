package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.models.dto.requests.DepartmentWithImageDTO;
import bg.tuvarna.models.entities.Department;
import bg.tuvarna.reporsitory.DepartmentRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.DepartmentService;
import bg.tuvarna.services.S3Service;
import bg.tuvarna.services.converters.impl.DepartmentConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentConverter converter;
    private final S3Service s3Service;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentConverter converter, S3Service s3Service) {
        repository = departmentRepository;
        this.converter = converter;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public void save(DepartmentWithImageDTO request) {
        DepartmentDTO department = request.getDto();

        if (department.id() != null) {
            Department entity = findDepartmentById(department.id());

            if (!Objects.equals(entity.getName(), department.name()) && findDepartmentByName(department.name()) != null) {
                throw new CustomException("Department already exists", ErrorCode.AlreadyExists);
            }

            repository.persist(converter.updateEntity(entity, department));

            setImage(entity, request);
            repository.persist(entity);
        } else {
            if (findDepartmentByName(department.name()) != null) {
                throw new CustomException("Department already exists", ErrorCode.AlreadyExists);
            }

            Department entity = converter.convertToEntity(department);

            setImage(entity, request);
            repository.persist(entity);
        }
    }

    private void setImage(Department department, DepartmentWithImageDTO request) {
        if (request.getImage() != null) {
            try {
                s3Service.uploadToS3("department-images", request.getImage(), "image_" + department.getName());

                department.setImageUrl("image_" + department.getName());
            } catch (IOException e) {

            }
        }
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
