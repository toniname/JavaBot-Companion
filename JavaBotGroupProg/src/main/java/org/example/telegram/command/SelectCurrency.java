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

public class SelectCurrency extends BotCommand {
    public SelectCurrency() {
        super("selectCurrency", "select Currency");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Select currency";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());
        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat.getId());

        InlineKeyboardButton btnUSD = InlineKeyboardButton
                .builder()
                .text("USD" + selectedOptions.isSelectedCurrency("USD"))
                .callbackData("currency usd")
                .build();

        InlineKeyboardButton btnEUR = InlineKeyboardButton
                .builder()
                .text("EUR" + selectedOptions.isSelectedCurrency("EUR"))
                .callbackData("currency eur")
                .build();


        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(btnUSD, btnEUR)))
                .build();

        sm.setReplyMarkup(ikm);

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }
}

