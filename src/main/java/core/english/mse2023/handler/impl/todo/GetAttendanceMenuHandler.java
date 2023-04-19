package core.english.mse2023.handler.impl.todo;

import core.english.mse2023.aop.annotation.handler.AllRegisteredRoles;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.component.InlineKeyboardMaker;
import core.english.mse2023.constant.InlineButtonCommand;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

@Component
@InlineButtonType
@AllRegisteredRoles
@RequiredArgsConstructor
public class GetAttendanceMenuHandler implements Handler {

    // TODO: должно быть заменено командой "Установить результаты занятия",
    //  в которой логика из этого хэндлера является одной из частей выполнения команды

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public List<BotApiMethod<?>> handle(Update update, UserRole userRole) {
        InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());
        return List.of(
                EditMessageReplyMarkup.builder()
                        .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                        .messageId(update.getCallbackQuery().getMessage().getMessageId())
                        .replyMarkup(inlineKeyboardMaker.getLessonAttendanceMenu(buttonData.getData()))
                        .build()
        );
    }

    @Override
    public BotCommand getCommandObject() {
        return InlineButtonCommand.GET_ATTENDANCE_MENU;
    }
}
