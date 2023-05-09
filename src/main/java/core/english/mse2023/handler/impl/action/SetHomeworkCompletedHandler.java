package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.component.MessageTextMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.Lesson;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@StudentRole
@RequiredArgsConstructor
public class SetHomeworkCompletedHandler implements Handler {

    private static final String DONE_TEXT = "Домашнее задание для урока \"%s\" установлено, как выполненное.";

    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        Lesson lesson = lessonService.setLessonHomeworkCompletion(lessonId, true);

        List<BotApiMethod<?>> actions = new ArrayList<>();

        actions.add(SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(String.format(DONE_TEXT, lesson.getTopic()))
                .build()
        );

        if (buttonData.getStateIndex() == 0) {
            actions.add(EditMessageReplyMarkup.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                            .messageId(update.getCallbackQuery().getMessage().getMessageId())
                            .replyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                    lessonService.getLessonById(lessonId),
                                    lessonService.getLessonInfoByLessonId(lessonId),
                                    userRole
                            ))
                    .build());
        } else if (buttonData.getStateIndex() == 1) {
            actions.add(EditMessageReplyMarkup.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .replyMarkup(inlineKeyboardMaker.getTaskMenu(lessonService.getLessonInfoByLessonId(lessonId)))
                    .build());
        }

        actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return actions;
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_HOMEWORK_COMPLETED;
    }
}
