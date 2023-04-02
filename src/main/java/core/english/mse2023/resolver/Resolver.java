package core.english.mse2023.resolver;

import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.cache.CacheData;
import core.english.mse2023.cache.CacheManager;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Resolver {

    private final CacheManager cacheManager;
    private final Map<BotCommand, Handler> textCommandHandlers;
    private final Map<String, Handler> inlineButtonsHandlers;

    private static final String NO_COMMAND_MESSAGE_TEXT = "Введенный текст не является командой или у вас недостаточно прав для ее использования.";

    public Resolver(List<Handler> textCommandHandlers, List<Handler> inlineButtonsHandlers) {
        this.textCommandHandlers = textCommandHandlers
                .stream()
                .collect(Collectors.toMap(
                        Handler::getCommandObject, Function.identity()
                ));
        this.inlineButtonsHandlers = inlineButtonsHandlers
                .stream()
                .collect(Collectors.toMap(
                        (handler -> handler.getCommandObject().getCommand()), Function.identity()
                ));
        this.cacheManager = new CacheManager();
    }

    private Handler getHandler(String command) {
        BotCommand searchingCommand = new BotCommand();
        for (BotCommand botCommand : textCommandHandlers.keySet()) {
            if (botCommand.getCommand().equals(command) || botCommand.getDescription().equals(command)) {
                searchingCommand = botCommand;
                break;
            }
        }
        return textCommandHandlers.get(searchingCommand);
    }

    public List<BotApiMethod<?>> resolve(Update update) {
        List<BotApiMethod<?>> reply = new ArrayList<>();

        if (update.hasMessage() && update.getMessage().hasText()) {

            String command = update.getMessage().getText();

            Handler handler = getHandler(command);

            if (handler != null) {

                if (handler instanceof InteractiveHandler interactiveHandler) {

                    cacheManager.cache(update.getMessage().getFrom().getId().toString(), new CacheData(interactiveHandler));

                    reply = interactiveHandler.handle(update);


                } else {
                    reply = handler.handle(update);
                }

            } else {
                // If the message is not a command

                // Checking if any command is in progress for this user
                CacheData commandData = cacheManager.getIfPresent(update.getMessage().getFrom().getId().toString());

                // If command found - proceed the command
                if (commandData != null) {
                    reply = commandData.updateData(update);
                    cacheManager.triggerTimeBasedEvictionChecker(update.getMessage().getFrom().getId().toString());
                } else {
                    reply = List.of(SendMessage.builder()
                            .chatId(update.getMessage().getChatId().toString())
                            .text(NO_COMMAND_MESSAGE_TEXT)
                            .build());
                }


            }

        } else if (update.hasCallbackQuery()) {
            // If we received an inline button press

            CacheData cacheData = cacheManager.getIfPresent(update.getCallbackQuery().getFrom().getId().toString());

            // Checking if user who pressed the button has any ongoing processes
            if (cacheData != null) {
                InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

                // Checking if data from the button corresponds with the expected data
                if (cacheData.getHandler().getCommandObject().getCommand().equals(inlineButtonDTO.getCommand()) &&
                        (cacheData.getState().getStateIndex() - 1) == inlineButtonDTO.getStateIndex()) {

                    // If everything is ok - proceed the command
                    reply = cacheData.updateData(update);

                    // By this if the command finished it work it can be deleted from cache
                    cacheManager.triggerTimeBasedEvictionChecker(update.getCallbackQuery().getFrom().getId().toString());
                }
            } else {
                InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

                Handler handler = inlineButtonsHandlers.get(buttonData.getCommand());

                if (handler != null) {
                    reply = handler.handle(update);
                }
            }
        }

        return reply;
    }

    public abstract UserRole getResolverUserRole();

}
