package org.example.telegram.command;

import org.example.telegram.CurrencyTelegramBot;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectBank extends BotCommand {

    public SelectBank() {
        super("selectBank", "Вибір банку");
    }

    private static final Logger logger = Logger.getLogger(SelectBank.class.getName());

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = "\uD83C\uDFE6 Виберіть банк \uD83C\uDFE6";
        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(chat.getId());
        SelectedOptions selectedOptions = CurrencyTelegramBot.getUsersOptions().get(chat.getId());
        InlineKeyboardMarkup ikm = createBankButtons(selectedOptions);
        sm.setReplyMarkup(ikm);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chat.getId().toString());
        sendPhoto.setPhoto(new InputFile(new File("JavaBotGroupProg/src/main/java/org/example/images/banks.png")));
        try {
            absSender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.out.println("Error");
        }

        try {
            absSender.execute(sm);
        } catch (TelegramApiException e) {
            logger.log(Level.SEVERE, "Telegram API error", e);
        }
    }

    private InlineKeyboardMarkup createBankButtons(SelectedOptions selectedOptions) {
        List<List<InlineKeyboardButton>> buttons = List.of(
                createButton("⬛ MоноБанк  ", "mono", selectedOptions.getSelectedBank()),
                createButton("\uD83D\uDFE9 ПриватБанк  ", "private", selectedOptions.getSelectedBank()),
                createButton("\uD83C\uDFE6 НБУ  ", "nbu", selectedOptions.getSelectedBank()),
                createButton("\uD83D\uDD19", "back", "")
        );
        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    private List<InlineKeyboardButton> createButton(String bankName, String callbackData, String selectedBank) {
        String buttonText = bankName + (selectedBank.equalsIgnoreCase(callbackData) ? "✅" : "");
        return List.of(InlineKeyboardButton.builder().text(buttonText).callbackData(callbackData).build());
    }
}