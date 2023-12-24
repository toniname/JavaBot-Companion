package org.example.telegram.command;

import org.example.telegram.CurrencyTelegramBot;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SelectPrecisoin extends BotCommand {

    public SelectPrecisoin() {
        super("/setprecision", "set n of digits after floating point");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Виберіть число знаків після коми";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());
        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat.getId());

        InlineKeyboardButton btn1 = InlineKeyboardButton
                .builder()
                .text("1" + selectedOptions.isSelectedPrecision("1"))
                .callbackData("setprecision 1")
                .build();

        InlineKeyboardButton btn2 = InlineKeyboardButton
                .builder()
                .text("2" + selectedOptions.isSelectedPrecision("2"))
                .callbackData("setprecision 2")
                .build();

        InlineKeyboardButton btn3 = InlineKeyboardButton
                .builder()
                .text("3" + selectedOptions.isSelectedPrecision("3"))
                .callbackData("setprecision 3")
                .build();

        InlineKeyboardButton btn4 = InlineKeyboardButton
                .builder()
                .text("4" + selectedOptions.isSelectedPrecision("4"))
                .callbackData("setprecision 4")
                .build();

        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(btn1), List.of(btn2), List.of(btn3), List.of(btn4)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
