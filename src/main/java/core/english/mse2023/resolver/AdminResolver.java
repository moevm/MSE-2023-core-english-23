package core.english.mse2023.resolver;

import core.english.mse2023.aop.annotation.handler.AdminRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.StudentRole;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AdminResolver extends Resolver {
    public AdminResolver(
            @TextCommandType @AdminRole List<Handler> textCommandHandlers,
            @InlineButtonType @AdminRole List<Handler> inlineButtonsHandlers
    ) {
        super(textCommandHandlers, inlineButtonsHandlers);
    }

    @Override
    public UserRole getResolverUserRole() {
        return UserRole.ADMIN;
    }
}
