package core.english.mse2023.resolver;

import core.english.mse2023.cache.CacheData;
import core.english.mse2023.cache.CacheManager;
import core.english.mse2023.constant.Command;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.NoSuchCommandException;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.model.dictionary.UserRole;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Resolver {

    private final CacheManager cacheManager;
    private final Map<Command, Handler> textCommandHandlers;
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
        for (Command botCommand : textCommandHandlers.keySet()) {
            if (botCommand.getCommand().equals(command) || botCommand.getDescription().equals(command)) {
                return textCommandHandlers.get(botCommand);
            }
        }

        throw new NoSuchCommandException();
    }

    public List<PartialBotApiMethod<?>> resolve(Update update, UserRole role) {
        List<PartialBotApiMethod<?>> reply = new ArrayList<>();

        if (update.hasMessage() && update.getMessage().hasText()) {

            String command = update.getMessage().getText();

            try {
                Handler handler = getHandler(command);

                if (handler instanceof InteractiveHandler interactiveHandler) {

                    CacheData commandData = new CacheData(interactiveHandler);
                    cacheManager.cache(update.getMessage().getFrom().getId().toString(), commandData);

                    reply = commandData.handleData(update, role);
                    cacheManager.triggerTimeBasedEvictionChecker(update.getMessage().getFrom().getId().toString());

                } else {
                    reply = handler.handle(update, role);
                }
            } catch (NoSuchCommandException noSuchCommandException) {
                // If the message is not a command

                // Checking if any command is in progress for this user
                CacheData commandData = cacheManager.getIfPresent(update.getMessage().getFrom().getId().toString());

                // If command found - proceed the command
                if (commandData != null) {
                    reply = commandData.updateData(update, role);
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
            InlineButtonDTO inlineButtonDTO = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

            // Checking if user who pressed the button has any ongoing processes
            if (cacheData != null && cacheData.getHandler().getCommandObject().getCommand().equals(inlineButtonDTO.getCommand())) {
                if (cacheData.getCurrentStateIndex() == inlineButtonDTO.getStateIndex()) {
                    // If everything is ok - proceed the command
                    reply = cacheData.updateData(update, role);

                    // By this if the command finished it work it can be deleted from cache
                    cacheManager.triggerTimeBasedEvictionChecker(update.getCallbackQuery().getFrom().getId().toString());
                }
            } else {
                InlineButtonDTO buttonData = InlineButtonDTOEncoder.decode(update.getCallbackQuery().getData());

                Handler handler = inlineButtonsHandlers.get(buttonData.getCommand());

                if (handler != null) {
                    if (handler instanceof InteractiveHandler interactiveHandler) {

                        CacheData commandData = new CacheData(interactiveHandler);
                        cacheManager.cache(update.getCallbackQuery().getFrom().getId().toString(), commandData);

                        reply = commandData.handleData(update, role);
                        cacheManager.triggerTimeBasedEvictionChecker(update.getCallbackQuery().getFrom().getId().toString());

                    } else {
                        reply = handler.handle(update, role);
                    }
                }
            }
        }

        return reply;
    }

    public abstract UserRole getResolverUserRole();

}
