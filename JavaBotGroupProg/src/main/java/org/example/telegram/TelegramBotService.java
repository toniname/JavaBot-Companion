package org.example.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBotService {

    CurrencyTelegramBot ctb;

    public TelegramBotService () {
        ctb = new CurrencyTelegramBot();
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(ctb);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}