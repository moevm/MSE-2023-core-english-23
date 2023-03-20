package core.english.mse2023.util.factory;

import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramInlineButtonsUtils {
    public static InlineKeyboardButton createInlineButton(String commandName, String data, int stateIndex, String text) {
        return InlineKeyboardButton.builder()
                .callbackData(InlineButtonDTOEncoder.encode(
                        InlineButtonDTO.builder()
                                .command(commandName)
                                .stateIndex(stateIndex)
                                .data(data)
                                .build()
                ))
                .text(text)
                .build();
    }
}
