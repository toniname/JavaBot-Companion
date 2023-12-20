package org.example.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import static org.example.Main.log;


public class CurrencyTelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return LoginConstants.NAME;
    }

    @Override
    public String getBotToken() {
        return LoginConstants.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String textFromUser = update.getMessage().getText();

            Long userId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getFrom().getFirstName();

            log.info("[{}, {}] : {}", userId, userFirstName, textFromUser);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(userId.toString())
                    .text("Привіт " + userFirstName + ", Ви написали: " + textFromUser)
                    .build();
            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Exception when sending message: ", e);
            }
        } else {
            log.warn("Unexpected update from user");
        }
    }

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//        System.out.println("Some updates recived");
//    }

}
