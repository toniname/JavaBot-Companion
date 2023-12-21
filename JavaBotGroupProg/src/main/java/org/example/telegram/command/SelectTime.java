package org.example.telegram.command;

import org.example.telegram.CurrencyTelegramBot;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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

    private KeyboardButton creatKeyboardButton (String txt){
        return KeyboardButton
                .builder()
                .text(txt)
                .build();
    }

    private  ReplyKeyboardMarkup createKeyboardMarkup() {
        KeyboardButton btn1 = creatKeyboardButton("9");
        KeyboardButton btn2 = creatKeyboardButton("10");
        KeyboardButton btn3 = creatKeyboardButton("11");
        KeyboardButton btn4 = creatKeyboardButton("12");
        KeyboardButton btn5 = creatKeyboardButton("13");
        KeyboardButton btn6 = creatKeyboardButton("14");
        KeyboardButton btn7 = creatKeyboardButton("15");
        KeyboardButton btn8 = creatKeyboardButton("16");
        KeyboardButton btn9 = creatKeyboardButton("17");
        KeyboardButton btn10 = creatKeyboardButton("18");
        KeyboardButton btn11 = creatKeyboardButton("Вимкнути сповіщення");
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(btn1);
        keyboardRow1.add(btn2);
        keyboardRow1.add(btn3);
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(btn4);
        keyboardRow2.add(btn5);
        keyboardRow2.add(btn6);
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(btn7);
        keyboardRow3.add(btn8);
        keyboardRow3.add(btn9);
        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(btn10);
        keyboardRow4.add(btn11);

        ReplyKeyboardMarkup ikm = ReplyKeyboardMarkup.builder()
                .keyboard(List.of(keyboardRow1, keyboardRow2, keyboardRow3, keyboardRow4 ))
                .build();
        return ikm;
    }
}