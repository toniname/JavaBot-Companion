package org.example.telegram.command;

import org.example.telegram.CurrencyTelegramBot;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SelectTime extends BotCommand {

    public SelectTime() {
        super("selecttime", "Select Time");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "\uD83D\uDD50 Виберіть час \uD83D\uDD50";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());
        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat.getId());


        sm.setReplyMarkup(createKeyboardMarkup());

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }

    private KeyboardButton creatKeyboardButton (String txt){
        return KeyboardButton
                .builder()
                .text(txt)
                .build();
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        List<KeyboardRow> rows = new ArrayList<>();

        for (int i = 9; i <= 18; i++) {
            KeyboardButton button = creatKeyboardButton(Integer.toString(i));
            KeyboardRow row = new KeyboardRow();
            row.add(button);
            rows.add(row);
        }

        KeyboardButton disableButton = creatKeyboardButton("Вимкнути сповіщення");
        KeyboardRow disableRow = new KeyboardRow();
        disableRow.add(disableButton);
        rows.add(disableRow);

        return ReplyKeyboardMarkup.builder().keyboard(rows).build();
    }
}

