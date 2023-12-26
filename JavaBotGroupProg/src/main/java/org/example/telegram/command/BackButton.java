package org.example.telegram.command;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class BackButton {
    public static InlineKeyboardButton createBackButton() {
        return InlineKeyboardButton.builder()
                .text("\uD83D\uDD19")
                .callbackData("back")
                .build();
    }
}
