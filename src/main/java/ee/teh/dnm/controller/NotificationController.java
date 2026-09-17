package ee.teh.dnm.controller;

import ee.teh.dnm.dto.NotificationAdminResponse;
import ee.teh.dnm.dto.NotificationMapper;
import ee.teh.dnm.dto.NotificationRequest;
import ee.teh.dnm.dto.NotificationResponse;
import ee.teh.dnm.model.Notification;
import ee.teh.dnm.model.NotificationStatus;
import ee.teh.dnm.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class NotificationController {
  private final NotificationService service;
  private static final String SUPPORT_CONTACT = "support@teh.ee";

  public NotificationController(NotificationService service) {
    this.service = service;
  }

  @GetMapping("/api/notification")
  public NotificationResponse getNotification() {
    Optional<Notification> notification = service.getActiveNotification();

    if (notification.isPresent()) {
      Notification n = notification.get();
      String title = n.getTitle();
      String content = n.getContent();
      LocalDateTime timestamp = LocalDateTime.now();

      return new NotificationResponse(NotificationStatus.ACTIVE, title, content, timestamp, SUPPORT_CONTACT);
    } else {
      return new NotificationResponse(NotificationStatus.OPERATIONAL, null, null, LocalDateTime.now(), SUPPORT_CONTACT);
    }
  }

  @GetMapping("/api/admin/notification")
  public List<NotificationAdminResponse> getAllNotifications() {
    return service.getAll().stream()
            .map(NotificationMapper::toAdminResponse)
            .toList();
  }

  @GetMapping("/api/admin/notification/{id}")
  public NotificationAdminResponse getNotification(@PathVariable Long id) {
    Notification notification = service.getNotification(id);
    return NotificationMapper.toAdminResponse(notification);
  }

  @PostMapping("/api/admin/notification")
  public NotificationAdminResponse addNotification(@RequestBody NotificationRequest request) {
    Notification notification = new Notification();
    notification.setTitle(request.title());
    notification.setContent(request.content());
    notification.setActive(request.active());
    Notification saved = service.save(notification);
    return NotificationMapper.toAdminResponse(saved);
  }

  @PutMapping("/api/admin/notification/{id}")
  public NotificationAdminResponse changeNotification(@PathVariable Long id, @RequestBody NotificationRequest request) {
    Notification notification = new Notification();
    notification.setTitle(request.title());
    notification.setContent(request.content());
    notification.setActive(request.active());

    Notification changed = service.change(id, notification);
    return NotificationMapper.toAdminResponse(changed);
  }

  @DeleteMapping("/api/admin/notification/{id}")
  public void deleteNotification(@PathVariable Long id) {
    service.delete(id);
  }

}
