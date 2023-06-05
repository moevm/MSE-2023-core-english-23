package core.english.mse2023.gateway;

import core.english.mse2023.exception.NoSuchUserException;
import core.english.mse2023.model.dictionary.UserRole;
import core.english.mse2023.resolver.Resolver;
import core.english.mse2023.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Gateway {

    @Value("${gateway.failed-to-identify-text}")
    private String failedToIdentifyText;

    private final UserService userService;

    private final Map<UserRole, Resolver> resolvers;


    public Gateway(UserService userService, List<Resolver> resolvers) {
        this.userService = userService;
        this.resolvers = resolvers.stream().collect(Collectors.toMap(
                Resolver::getResolverUserRole, Function.identity()
        ));
    }

    public List<PartialBotApiMethod<?>> processUpdate(Update update) {
        List<PartialBotApiMethod<?>> reply = new ArrayList<>();

        if (update.hasMessage()) {
            try {
                UserRole userRole = userService.getUserRole(update.getMessage().getFrom().getId().toString());

                userService.setChatIdForUser(update.getMessage().getFrom().getId().toString(), update.getMessage().getChatId().toString());

                reply = resolvers.get(userRole).resolve(update, userRole);
            } catch (NoSuchUserException exception) {
                reply = resolvers.get(UserRole.GUEST).resolve(update, UserRole.GUEST);
            }
        } else if(update.hasCallbackQuery()) {

            try {
                UserRole userRole = userService.getUserRole(update.getCallbackQuery().getFrom().getId().toString());

                userService.setChatIdForUser(update.getCallbackQuery().getFrom().getId().toString(), update.getCallbackQuery().getMessage().getChatId().toString());

                reply = resolvers.get(userRole).resolve(update, userRole);
            } catch (NoSuchUserException exception) {
                reply = List.of(SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text(failedToIdentifyText)
                        .build());
            }
        }

        return reply;
    }

}
