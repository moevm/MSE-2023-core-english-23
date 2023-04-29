package core.english.mse2023.service.impl;

import core.english.mse2023.aop.aspect.Notification;
import core.english.mse2023.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StubNotificationService implements NotificationService {

    @Override
    public void sendNotification(UUID userId, Notification notification) {
        log.info("типа уведомил " + userId);
    }
}
