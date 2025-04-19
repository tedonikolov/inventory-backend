package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.ChangeRoleDTO;
import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.dto.requests.EmployeeWithImageDTO;
import bg.tuvarna.models.entities.Department;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.reporsitory.EmployeeRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.DepartmentService;
import bg.tuvarna.services.EmployeeServices;
import bg.tuvarna.services.S3Service;
import bg.tuvarna.services.converters.impl.EmployeeConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeServicesImpl implements EmployeeServices {
    private final EmployeeRepository repository;
    private final KeycloakService keycloakService;
    private final EmployeeConverter converter;
    private final DepartmentService departmentService;
    private final S3Service s3Service;

    public EmployeeServicesImpl(EmployeeRepository repository, KeycloakService keycloakService, EmployeeConverter converter, DepartmentService departmentService, S3Service s3Service) {
        this.repository = repository;
        this.keycloakService = keycloakService;
        this.converter = converter;
        this.departmentService = departmentService;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public void save(CreateUserDTO createUserDTO) {
        keycloakService.registerUser(createUserDTO);

        Employee employee = new Employee();
        employee.setUsername(createUserDTO.getUsername());
        employee.setEmail(createUserDTO.getEmail());
        employee.setFullName(createUserDTO.getFullName());
        employee.setPosition(createUserDTO.getEmployeePosition());

        Department department = departmentService.findDepartmentById(createUserDTO.getDepartmentId());
        employee.setDepartment(department);

        repository.persist(employee);
    }

    @Override
    @Transactional
    public void update(EmployeeWithImageDTO request) {
        EmployeeDTO employeeDTO = request.getDto();

        Employee employee = findEmployeeById(employeeDTO.id());
        if (employee.getPosition() != employeeDTO.employeePosition()) {
            keycloakService.changeRole(new ChangeRoleDTO(employee.getEmail(), employee.getPosition(), employeeDTO.employeePosition()));
        }

        if (request.getImage() != null) {
            try {
                s3Service.uploadToS3("employee-images", request.getImage(), "image_" + employee.getUsername());

                employee.setImageUrl("image_" + employee.getUsername());
            } catch (IOException e) {

            }
        }

        repository.persist(converter.updateEntity(employee, employeeDTO));
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return converter.convertToDto(findEmployeeById(id));
    }

    @Override
    public EmployeeDTO getEmployeeByUsername(String username) {
        return repository.find("username",username).firstResultOptional()
                .map(converter::convertToDto)
                .orElseThrow(() -> new CustomException("Employee not found", ErrorCode.EntityNotFound));
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return repository.findByIdOptional(id).orElseThrow(() ->
                new CustomException("Employee not found", ErrorCode.EntityNotFound));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return repository.findAll().stream().map(converter::convertToDto).collect(Collectors.toList());
    }
}
