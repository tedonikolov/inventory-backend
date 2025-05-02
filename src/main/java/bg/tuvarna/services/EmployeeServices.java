package bg.tuvarna.services;

import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.dto.requests.CreateUserDTO;
import bg.tuvarna.models.dto.requests.EmployeeWithImageDTO;
import bg.tuvarna.models.entities.Employee;

import java.util.List;

public interface EmployeeServices {
    void save(CreateUserDTO employeeDTO);
    void update(EmployeeWithImageDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO getEmployeeByUsername(String username);
    Employee findEmployeeById(Long id);
    List<EmployeeDTO> getAllEmployees();
    List<EmployeeDTO> getAllEmployeesForDepartment(Long departmentId);
}
