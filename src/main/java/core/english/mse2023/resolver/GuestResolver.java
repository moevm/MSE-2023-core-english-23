package core.english.mse2023.resolver;

import core.english.mse2023.aop.annotation.handler.GuestRole;
import core.english.mse2023.aop.annotation.handler.InlineButtonType;
import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GuestResolver extends Resolver{
    public GuestResolver(
            @TextCommandType @GuestRole List<Handler> textCommandHandlers,
            @InlineButtonType @GuestRole List<Handler> inlineButtonsHandlers
    ) {
        super(textCommandHandlers, inlineButtonsHandlers);
    }

    @Override
    public UserRole getResolverUserRole() {
        return UserRole.GUEST;
    }
}
