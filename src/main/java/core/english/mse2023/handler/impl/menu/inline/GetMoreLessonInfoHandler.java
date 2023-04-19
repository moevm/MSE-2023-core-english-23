package core.english.mse2023.handler.impl.menu.inline;

import core.english.mse2023.aop.annotation.handler.AllRegisteredRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
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
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.UUID;

@Component
@InlineButtonType
@AllRegisteredRoles
@RequiredArgsConstructor
public class GetMoreLessonInfoHandler implements Handler {

    private final MessageTextMaker messageTextMaker;

    private final LessonService lessonService;

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {

        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

        Lesson lesson = lessonService.getLessonById(UUID.fromString(buttonData.getData()));


        SendMessage message = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                .text(messageTextMaker.lessonInfoPatternMessageText(lesson))
                .build();

        message.setParseMode(ParseMode.MARKDOWNV2);

        message.setReplyMarkup(inlineKeyboardMaker.getLessonMainMenuInlineKeyboard(
                lesson,
                lessonService.getLessonInfoByLessonId(lesson.getId()),
                userRole
        ));

        return List.of(message, new AnswerCallbackQuery(update.getCallbackQuery().getId()));
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.GET_MORE_LESSON_INFO;
    }
}
