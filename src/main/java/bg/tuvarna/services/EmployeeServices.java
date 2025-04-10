package bg.tuvarna.services;

import bg.tuvarna.models.dto.EmployeeDTO;
import bg.tuvarna.models.entities.Employee;

import java.util.List;

public interface EmployeeServices {
    void save(EmployeeDTO employeeDTO);
    void update(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    Employee findEmployeeById(Long id);
    List<EmployeeDTO> getAllEmployees();
}
