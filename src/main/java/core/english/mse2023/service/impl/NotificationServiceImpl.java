package core.english.mse2023.service.impl;

import core.english.mse2023.aop.aspect.Notification;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.NotificationService;
import core.english.mse2023.service.UserService;
import core.english.mse2023.tgbot.TelegramBot;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private static final String lessonDateChangedNotificationText = "Урок \"%s\" с одной из ваших подписок сменил дату проведения на %s";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    private final TelegramBot telegramBot;

    private final LessonService lessonService;

    @Override
    @Transactional
    public void sendLessonDateChangedNotification(UUID lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);

        log.info("Sending Lesson Date Changed notification to student with telegram id: " + lesson.getSubscription().getStudent().getTelegramId());

        telegramBot.executeBotApiMethods(List.of(SendMessage.builder()
                .chatId(lesson.getSubscription().getStudent().getChatId())
                .text(String.format(lessonDateChangedNotificationText, lesson.getTopic(), dateFormat.format(lesson.getDate())))
                .build()));
    }
}
