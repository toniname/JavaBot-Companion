package org.example.telegram.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;

public class SettingsCommand extends BotCommand {

    public SettingsCommand() {
        super("settings", "setings of the options");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Select option";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        InlineKeyboardButton btn1 = InlineKeyboardButton
                .builder()
                .text("Bank")
                .callbackData("bank")
                .build();
        InlineKeyboardButton btn2 = InlineKeyboardButton
                .builder()
                .text("Currency")
                .callbackData("currency")
                .build();

        InlineKeyboardButton btn3 = InlineKeyboardButton
                .builder()
                .text("Time")
                .callbackData("time")
                .build();

        InlineKeyboardButton btn4 = InlineKeyboardButton
                .builder()
                .text("Precision")
                .callbackData("precision")
                .build();


        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(btn1, btn2, btn3, btn4)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
