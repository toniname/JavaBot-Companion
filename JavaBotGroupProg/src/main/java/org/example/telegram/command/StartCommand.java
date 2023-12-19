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

public class StartCommand extends BotCommand {

    public StartCommand() {
        super("start", "start command will initiate currency bot");
    }


    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Select currency you want to check";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());

        InlineKeyboardButton usdButton = InlineKeyboardButton
                .builder()
                .text("Settings")
                .callbackData("settings")
                .build();
        InlineKeyboardButton eurButton = InlineKeyboardButton
                .builder()
                .text("Get info")
                .callbackData("info")
                .build();

        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(Arrays.asList(usdButton, eurButton)))
                .build();

        sm.setReplyMarkup(ikm);

        CurrencyTelegramBot.getUsersOptions().put(chat, new SelectedOptions());

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }

    }
}
