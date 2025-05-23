package bg.tuvarna.services.impl;

import bg.tuvarna.models.dto.NotificationDTO;
import bg.tuvarna.models.entities.Employee;
import bg.tuvarna.models.entities.Notification;
import bg.tuvarna.reporsitory.NotificationRepository;
import bg.tuvarna.services.EmployeeServices;
import bg.tuvarna.services.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationSenderService notificationSenderService;
    private final EmployeeServices employeeServices;

    public NotificationServiceImpl(NotificationSenderService notificationSenderService, NotificationRepository notificationRepository, EmployeeServices employeeServices) {
        this.notificationSenderService = notificationSenderService;
        this.notificationRepository = notificationRepository;
        this.employeeServices = employeeServices;
    }

    @Transactional
    public void createNotify(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setTitle(notificationDTO.title());
        notification.setContent(notificationDTO.content());
        notification.setType(notificationDTO.type());

        Employee employee = employeeServices.findEmployeeById(notificationDTO.employeeId());

        notification.setEmployee(employee);

        notificationRepository.persist(notification);

        if (employee.getPhoneToken() != null) {
            notificationSenderService.sendPushNotification(
                    employee.getPhoneToken(),
                    notificationDTO.title(),
                    notificationDTO.content(),
                    notificationDTO.type()
            );
        }
    }

    @Override
    public List<NotificationDTO> selfNotification(Long id) {
        return notificationRepository.findByEmployeeId(id).stream().map(notification -> new NotificationDTO(
                        notification.id,
                        notification.getTitle(),
                        notification.getContent(),
                        notification.getType(),
                        notification.getEmployee().id,
                        notification.isRead(),
                        notification.getCreatedAt()
                )
        ).toList();
    }

    @Override
    public List<NotificationDTO> getAllForDepartment(Long departmentId) {
        return notificationRepository.findByDepartment(departmentId).stream().map(notification -> new NotificationDTO(
                        notification.id,
                        notification.getTitle(),
                        notification.getContent(),
                        notification.getType(),
                        notification.getEmployee().id,
                        notification.isRead(),
                        notification.getCreatedAt()
                )
        ).toList();
    }

    @Override
    @Transactional
    public void markRead(Long id) {
        Notification notification = notificationRepository.findById(id);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.persist(notification);
        }
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id);
        if (notification != null) {
            notification.setEmployee(null);
            notificationRepository.persist(notification);
            notificationRepository.delete(notification);
        }
    }
}