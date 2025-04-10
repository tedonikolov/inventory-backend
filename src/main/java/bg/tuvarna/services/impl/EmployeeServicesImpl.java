package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.ChangeRoleDTO;
import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.reporsitory.EmployeeRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.services.EmployeeServices;
import bg.tuvarna.services.converters.impl.EmployeeConverter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeServicesImpl implements EmployeeServices {
    private final EmployeeRepository repository;
    private final KeycloakService keycloakService;
    private final EmployeeConverter converter;

    public EmployeeServicesImpl(EmployeeRepository repository, KeycloakService keycloakService, EmployeeConverter converter) {
        this.repository = repository;
        this.keycloakService = keycloakService;
        this.converter = converter;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        keycloakService.registerUser(new CreateUserDTO(employeeDTO.username(), employeeDTO.email(), null, employeeDTO.employeePosition()));

        repository.persist(converter.convertToEntity(employeeDTO));
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = findEmployeeById(employeeDTO.id());
        if (employee.getPosition() != employeeDTO.employeePosition()) {
            keycloakService.changeRole(new ChangeRoleDTO(employee.getEmail(), employee.getPosition(), employeeDTO.employeePosition()));
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
