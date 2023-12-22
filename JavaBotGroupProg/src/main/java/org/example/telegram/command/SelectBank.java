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

import java.util.Arrays;
import java.util.Collections;

public class SelectBank extends BotCommand {

    public SelectBank() {
        super("selectBank", "Оберіть банк");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Выберите банк";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat.getId());
        if (selectedOptions == null) {
            selectedOptions = new SelectedOptions();
        }
        InlineKeyboardButton btn1 = InlineKeyboardButton
                .builder()
                .text("Mono" + selectedOptions.isBankSelected("mono"))
                .callbackData("selectBank mono")
                .build();

        InlineKeyboardButton btn2 = InlineKeyboardButton
                .builder()
                .text("Pryvat" + selectedOptions.isBankSelected("pryvat"))
                .callbackData("selectBank pryvat")
                .build();

        InlineKeyboardButton btn3 = InlineKeyboardButton
                .builder()
                .text("NBU" + selectedOptions.isBankSelected("nbu"))
                .callbackData("selectBank nbu")
                .build();

        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(btn1, btn2, btn3)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}
