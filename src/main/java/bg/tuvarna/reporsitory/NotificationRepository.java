package bg.tuvarna.reporsitory;

import bg.tuvarna.models.entities.Notification;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<Notification> {

    public List<Notification> findByEmployeeId(long employeeId) {
        return find("employee.id", employeeId).list();
    }

    public List<Notification> findByDepartment(long departmentId) {
        return find("employee.department.id = ?1", departmentId).list();
    }
}