package core.english.mse2023.util.builder;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public final class InlineKeyboardBuilder {

    private final List<List<InlineKeyboardButton>> buttons;
    private int buildableRow = -1;

    private InlineKeyboardBuilder() {
        buttons = new ArrayList<>();
        row();
    }

    public static InlineKeyboardBuilder instance() {
        return new InlineKeyboardBuilder();
    }

    public InlineKeyboardBuilder row() {
        buttons.add(new ArrayList<>());
        buildableRow++;

        return this;
    }

    public InlineKeyboardBuilder button(String text) {
        return button(new InlineKeyboardButton(text));
    }

    public InlineKeyboardBuilder button(InlineKeyboardButton button) {
        buttons.get(buildableRow).add(button);

        return this;
    }

    public InlineKeyboardMarkup build() {
        return new InlineKeyboardMarkup(buttons);
    }

}
