package core.english.mse2023.tgbot;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.cache.CacheData;
import core.english.mse2023.config.BotConfig;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.gateway.Gateway;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.handler.InteractiveHandler;
import core.english.mse2023.resolver.Resolver;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    // Cache for storing last used commands with require users input

    private final Gateway gateway;

    public TelegramBot(BotConfig config, Gateway gateway) {
        this.config = config;
        this.gateway = gateway;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        executeBotApiMethods(gateway.processUpdate(update));
    }

    /**
     * Sends all the messages from the list
     *
     * @param methods - list of bot api methods to execute
     */
    public void executeBotApiMethods(List<BotApiMethod<?>> methods) {
        if (methods == null) return;
        for (BotApiMethod<?> method : methods) {
            try {
                execute(method);
                log.info("Reply sent");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }


}
