package core.english.mse2023.handler.impl.action;

import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.component.InlineKeyboardMaker;
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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@StudentRole
@RequiredArgsConstructor
public class SetHomeworkNotCompletedHandler implements Handler {

    private static final String DONE_TEXT = "Домашнее задание для урока \"%s\" установлено, как не выполненное.";

    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        Lesson lesson = lessonService.setLessonHomeworkCompletion(lessonId, false);

        List<BotApiMethod<?>> actions = new ArrayList<>();

        SendMessage workDoneMessage = createDoneMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                lesson.getTopic()
        );

        actions.add(workDoneMessage);

        if (buttonData.getStateIndex() == 0) {
            EditMessageReplyMarkup editMessageReplyMarkup = createEditReplyMarkupActionForLessonMenu(
                    update.getCallbackQuery().getMessage().getChatId().toString(),
                    update.getCallbackQuery().getMessage().getMessageId(),
                    lessonId,
                    userRole
            );

            actions.add(editMessageReplyMarkup);
        } else if (buttonData.getStateIndex() == 1) {
            EditMessageReplyMarkup editMessageReplyMarkup = createEditReplyMarkupActionForTaskMenu(
                    update.getCallbackQuery().getMessage().getChatId().toString(),
                    update.getCallbackQuery().getMessage().getMessageId(),
                    lessonId
            );

            actions.add(editMessageReplyMarkup);
        }

        actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return actions;
    }

    private EditMessageReplyMarkup createEditReplyMarkupActionForLessonMenu(String chatId, int messageId, UUID lessonId, UserRole userRole) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(
                        inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                lessonService.getLessonById(lessonId),
                                lessonService.getLessonInfoByLessonId(lessonId),
                                userRole
                        )
                )
                .build();
    }

    private EditMessageReplyMarkup createEditReplyMarkupActionForTaskMenu(String chatId, int messageId, UUID lessonId) {
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(
                        inlineKeyboardMaker.getTaskMenu(
                                lessonService.getLessonInfoByLessonId(lessonId)
                        )
                )
                .build();
    }

    private SendMessage createDoneMessage(String chatId, String lessonTopic) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(String.format(DONE_TEXT, lessonTopic))
                .build();
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_HOMEWORK_NOT_COMPLETED;
    }
}