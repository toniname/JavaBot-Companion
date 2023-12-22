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

public class SelectTime extends BotCommand {

    public SelectTime() {
        super("selecttime", "Select Time");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "Select Time";

        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());
        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat);

        sm.setReplyMarkup(createKeyboardMarkup());

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }
    }

    private InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    private InlineKeyboardMarkup createKeyboardMarkup() {
        InlineKeyboardButton btn1 = createInlineKeyboardButton("9", "9");
        InlineKeyboardButton btn2 = createInlineKeyboardButton("10", "10");
        InlineKeyboardButton btn3 = createInlineKeyboardButton("11", "11");
        InlineKeyboardButton btn4 = createInlineKeyboardButton("12", "12");
        InlineKeyboardButton btn5 = createInlineKeyboardButton("13", "13");
        InlineKeyboardButton btn6 = createInlineKeyboardButton("14", "14");
        InlineKeyboardButton btn7 = createInlineKeyboardButton("15", "15");
        InlineKeyboardButton btn8 = createInlineKeyboardButton("16", "16");
        InlineKeyboardButton btn9 = createInlineKeyboardButton("17", "17");
        InlineKeyboardButton btn10 = createInlineKeyboardButton("18", "18");
        InlineKeyboardButton btn11 = createInlineKeyboardButton("Вимкнути сповіщення", "disable_notifications");

        List<List<InlineKeyboardButton>> keyboard = List.of(
                List.of(btn1, btn2, btn3),
                List.of(btn4, btn5, btn6),
                List.of(btn7, btn8, btn9),
                List.of(btn10, btn11)
        );

        InlineKeyboardMarkup ikm = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();
        return ikm;
    }
}