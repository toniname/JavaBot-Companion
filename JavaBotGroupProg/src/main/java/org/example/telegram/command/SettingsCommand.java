package org.example.telegram.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SettingsCommand extends BotCommand {

    public SettingsCommand() {
        super("settings", "setings of the options");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "⚙ Вибір опції ⚙";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        InlineKeyboardButton btn1 = InlineKeyboardButton
                .builder()
                .text("Банк")
                .callbackData("bank")
                .build();
        InlineKeyboardButton btn2 = InlineKeyboardButton
                .builder()
                .text("Валюта")
                .callbackData("currency")
                .build();

        InlineKeyboardButton btn3 = InlineKeyboardButton
                .builder()
                .text("Час")
                .callbackData("time")
                .build();

        InlineKeyboardButton btn4 = InlineKeyboardButton
                .builder()
                .text("Число знаків після коми")
                .callbackData("precision")
                .build();


        InlineKeyboardButton backButton = BackButton.createBackButton();


        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(btn1), List.of(btn2), List.of(btn3), List.of(btn4), List.of(backButton)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
