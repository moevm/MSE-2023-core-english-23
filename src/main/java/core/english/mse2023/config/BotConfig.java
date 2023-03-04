package core.english.mse2023.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.yaml")
public class BotConfig {
    @Value("${bot.name}")
    private final String botName;
    @Value("${bot.token}")
    private final String token;
    @Value("${bot.chatId}")
    private final String chatId;
}
