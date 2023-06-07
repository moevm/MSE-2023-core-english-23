package core.english.mse2023.service.impl;

import core.english.mse2023.model.Lesson;
import core.english.mse2023.service.LessonService;
import core.english.mse2023.service.NotificationService;
import core.english.mse2023.tgbot.TelegramBot;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private static final String LESSON_DATE_CHANGED_NOTIFICATION_TEXT = "Урок \"%s\" с одной из ваших подписок сменил дату проведения на %s";
    private static final String UPCOMING_LESSON_NOTIFICATION_TEXT = "Урок \"%s\" с одной из ваших подписок начнется менее, чем через %s. Точное начало урока: %s";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private final LessonService lessonService;

    @Override
    @Transactional
    public SendMessage getLessonDateChangedNotificationMessage(UUID lessonId) {
        Lesson lesson = lessonService.getLessonById(lessonId);

        log.info("Sending Lesson Date Changed notification to student with telegram id: " + lesson.getSubscription().getStudent().getTelegramId());

        return SendMessage.builder()
                .chatId(lesson.getSubscription().getStudent().getChatId())
                .text(String.format(LESSON_DATE_CHANGED_NOTIFICATION_TEXT, lesson.getTopic(), dateFormat.format(lesson.getDate())))
                .build();
    }

    @Override
    public SendMessage getUpcomingLessonNotificationMessage(Lesson lesson, Duration threshold) {
        log.info("Sending Upcoming Lesson notification to student with telegram id: " + lesson.getSubscription().getStudent().getTelegramId());

        return SendMessage.builder()
                .chatId(lesson.getSubscription().getStudent().getChatId())
                .text(String.format(UPCOMING_LESSON_NOTIFICATION_TEXT,
                        lesson.getTopic(),
                        getThresholdText(threshold),
                        dateTimeFormat.format(lesson.getDate())))
                .build();
    }

    private String getThresholdText(Duration threshold) {

        if (threshold.toMinutes() == 0 && threshold.toHours() == 0 && threshold.toDays() == 0) {
            return "небольшое количество времени";
        }

        List<String> parts = new ArrayList<>();

        if (threshold.toDays() != 0) {
            long days = threshold.toDays();
            if (days % 10 == 1 && days % 100 != 11) {
                parts.add(days + " день");
            } else if ((days % 10 == 2 || days % 10 == 3 || days % 10 == 4) &&
                    !(days % 100 == 12 || days % 100 == 13 || days % 100 == 14)) {
                parts.add(days + " дня");
            } else {
                parts.add(days + " дней");
            }
        }

        if (threshold.toHours() % 24 != 0) {
            long hours = threshold.toHours() % 24;
            if (hours % 10 == 1 && hours % 100 != 11) {
                parts.add(hours + " час");
            } else if ((hours % 10 == 2 || hours % 10 == 3 || hours % 10 == 4) &&
                    !(hours % 100 == 12 || hours % 100 == 13 || hours % 100 == 14)) {
                parts.add(hours + " часа");
            } else {
                parts.add(hours + " часов");
            }
        }

        if (threshold.toMinutes() % 60 != 0) {
            long minutes = threshold.toMinutes() % 60;
            if (minutes % 10 == 1 && minutes % 100 != 11) {
                parts.add(minutes + " минута");
            } else if ((minutes % 10 == 2 || minutes % 10 == 3 || minutes % 10 == 4) &&
                    !(minutes % 100 == 12 || minutes % 100 == 13 || minutes % 100 == 14)) {
                parts.add(minutes + " минуты");
            } else {
                parts.add(minutes + " минут");
            }
        }

        return String.join(", ", parts);
    }
}
