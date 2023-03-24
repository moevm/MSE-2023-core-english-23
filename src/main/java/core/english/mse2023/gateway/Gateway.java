package core.english.mse2023.gateway;

import core.english.mse2023.aop.annotation.handler.TextCommandType;
import core.english.mse2023.dto.InlineButtonDTO;
import core.english.mse2023.encoder.InlineButtonDTOEncoder;
import core.english.mse2023.exception.NoSuchUserException;
import core.english.mse2023.handler.Handler;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.resolver.Resolver;
import core.english.mse2023.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Gateway {

    private final UserService userService;

    private final Map<UserRole, Resolver> resolvers;

    private static final String FAILED_TO_IDENTIFY_TEXT = "Не удалось найти вас в системе. Напишите /start для начала работы или свяжитесь со службой поддержки.";


    public Gateway(UserService userService, List<Resolver> resolvers) {
        this.userService = userService;
        this.resolvers = resolvers.stream().collect(Collectors.toMap(
                Resolver::getResolverUserRole, Function.identity()
        ));
    }

    public List<? extends BotApiMethod<?>> processUpdate(Update update) {
        List<? extends BotApiMethod<?>> reply = new ArrayList<>();

        if (update.hasMessage()) {
            try {
                UserRole userRole = userService.getUserRole(update.getMessage().getFrom().getId().toString());

                reply = resolvers.get(userRole).resolve(update);
            } catch (NoSuchUserException exception) {
                reply = List.of(SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text(FAILED_TO_IDENTIFY_TEXT)
                        .build());
            }
        } else if(update.hasCallbackQuery()) {
            try {
                UserRole userRole = userService.getUserRole(update.getCallbackQuery().getFrom().getId().toString());

                reply = resolvers.get(userRole).resolve(update);
            } catch (NoSuchUserException exception) {
                reply = List.of(SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text(FAILED_TO_IDENTIFY_TEXT)
                        .build());
            }
        }

        return reply;
    }

}
