package core.english.mse2023.resolver;

import core.english.mse2023.handler.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Resolver {
    private final Map<BotCommand, Handler> handlers;

    public Resolver(List<Handler> handlers) {
        this.handlers = handlers
                .stream()
                .collect(Collectors.toMap(Handler::getCommandObject, Function.identity()));
    }

    public Handler getHandler(String command) {
        BotCommand searchingCommand = new BotCommand();
        for (BotCommand botCommand: handlers.keySet()) {
            if (botCommand.getCommand().equals(command) || botCommand.getDescription().equals(command)) {
                searchingCommand = botCommand;
                break;
            }
        }
        return handlers.get(searchingCommand);
    }

}
