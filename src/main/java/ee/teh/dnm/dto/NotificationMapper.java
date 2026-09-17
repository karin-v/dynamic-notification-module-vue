package ee.teh.dnm.dto;

import ee.teh.dnm.model.Notification;

public class NotificationMapper {
  public static NotificationAdminResponse toAdminResponse(Notification notification) {
    return new NotificationAdminResponse(
            notification.getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.isActive()
    );
  }
}
