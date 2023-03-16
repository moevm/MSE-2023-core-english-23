package core.english.mse2023.resolver;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import core.english.mse2023.cache.CacheData;
import core.english.mse2023.config.BotConfig;
import core.english.mse2023.handler.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.aspectj.weaver.tools.cache.WeavedClassCache.createCache;

@Slf4j
@Component
public class Resolver {
    private final Map<BotCommand, Handler> handlers;

    public Resolver(List<Handler> handlers) {
        this.handlers = handlers
                .stream()
                .collect(Collectors.toMap(Handler::getCommand, Function.identity()));
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
