package ee.teh.dnm.service;

import ee.teh.dnm.model.Notification;
import ee.teh.dnm.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

  private final NotificationRepository repository;

  public NotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  public Optional<Notification> getActiveNotification() {
    return repository.findByActiveTrue();
  }

  public List<Notification> getAll() {
    return repository.findAllByOrderById();
  }

  public Notification getNotification(Long id) {
    return repository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found: " + id));
  }

  @Transactional
  public Notification save(Notification notification) {
    validate(notification);
    if (notification.isActive()) {
      activate(notification);
    }
    return repository.save(notification);
  }

  @Transactional
  public Notification change(Long id, Notification notification) {
    validate(notification);
    Notification existing = repository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found: " + id));
    existing.setTitle(notification.getTitle());
    existing.setContent(notification.getContent());

    if (notification.isActive()) {
      activate(existing);
    } else {
      existing.setActive(false);
    }
    return repository.save(existing);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  private void activate(Notification notification) {
    repository.findByActiveTrue().ifPresent(active -> {
      active.setActive(false);
      repository.save(active);
    });
    notification.setActive(true);
  }

  private void validate(Notification notification) {
    if (notification.getTitle() == null || notification.getTitle().isBlank()
            || notification.getContent() == null || notification.getContent().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title and content must not be blank");
    }
  }

}
