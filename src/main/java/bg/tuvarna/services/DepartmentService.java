package bg.tuvarna.services;

import bg.tuvarna.models.dto.DepartmentDTO;
import bg.tuvarna.models.dto.requests.DepartmentWithImageDTO;
import bg.tuvarna.models.entities.Department;

import java.util.List;

public interface DepartmentService {
    void save(DepartmentWithImageDTO department);

    void removeDepartment(Long id);

    List<DepartmentDTO> getAllDepartments();

    DepartmentDTO getDepartment(Long id);

    Department findDepartmentById(Long id);

    Department findDepartmentByName(String departmentName);
}
