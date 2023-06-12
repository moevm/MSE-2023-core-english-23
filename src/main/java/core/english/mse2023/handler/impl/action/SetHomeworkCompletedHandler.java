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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
@Slf4j
@RequiredArgsConstructor
public class SetHomeworkCompletedHandler implements Handler {

    @Value("${messages.handlers.set-homework-completed.done}")
    private String doneText;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    private final LessonService lessonService;

    @Override
    public List<PartialBotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        UUID lessonId = UUID.fromString(buttonData.getData());

        Lesson lesson = lessonService.setLessonHomeworkCompletion(lessonId, true);

        List<PartialBotApiMethod<?>> actions = new ArrayList<>();

        SendMessage workDoneMessage = createDoneMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                lesson.getTopic()
        );

        actions.add(workDoneMessage);

        EditMessageReplyMarkup editReplyMarkupActionForMenu = null;

        if (buttonData.getStateIndex() == 0) {
            // For Lesson Menu
            editReplyMarkupActionForMenu = EditMessageReplyMarkup.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .replyMarkup(
                            inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                                    lessonService.getLessonById(lessonId),
                                    lessonService.getLessonInfoByLessonId(lessonId),
                                    userRole
                            )
                    )
                    .build();
        } else if (buttonData.getStateIndex() == 1) {
            // For Task Menu
            editReplyMarkupActionForMenu = EditMessageReplyMarkup.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .replyMarkup(
                            inlineKeyboardMaker.getTaskMenu(
                                    lessonService.getLessonInfoByLessonId(lessonId)
                            )
                    )
                    .build();
        }

        if (editReplyMarkupActionForMenu == null) {
            log.error("Interactive button contains illegal state index: " + buttonData.getStateIndex() + ". Indexes 0 or 1 were expected.");
            throw new IllegalStateException("Interactive button contains illegal state index: " + buttonData.getStateIndex() + ". Indexes 0 or 1 were expected.");
        }

        actions.add(editReplyMarkupActionForMenu);

        actions.add(new AnswerCallbackQuery(update.getCallbackQuery().getId()));

        return actions;
    }

    private SendMessage createDoneMessage(String chatId, String lessonTopic) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(String.format(doneText, lessonTopic))
                .build();
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.SET_HOMEWORK_COMPLETED;
    }
}
