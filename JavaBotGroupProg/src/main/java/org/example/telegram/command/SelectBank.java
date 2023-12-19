package org.example.telegram.command;

import org.example.telegram.SelectedOptions;
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
import java.util.List;


public class SelectBank extends BotCommand {

    public SelectBank() {
        super("selectBank", "select specific bank");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Select bank";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        InlineKeyboardButton btn1 = InlineKeyboardButton
                .builder()
                .text("Mono" + SelectedOptions.isBankSelected("mono"))
                .callbackData("mono")
                .build();

        InlineKeyboardButton btn2 = InlineKeyboardButton
                .builder()
                .text("Pryvat" + SelectedOptions.isBankSelected("pryvat"))
                .callbackData("pryvat")
                .build();

        InlineKeyboardButton btn3 = InlineKeyboardButton
                .builder()
                .text("NBU" + SelectedOptions.isBankSelected("nbu"))
                .callbackData("nbu")
                .build();

        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(btn1), List.of(btn2), List.of(btn3)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
