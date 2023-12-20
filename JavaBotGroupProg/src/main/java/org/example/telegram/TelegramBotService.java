package org.example.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.example.Main.log;

public class TelegramBotService {

    public TelegramBotService () throws TelegramApiException {
        try {
            log.info("Registering bot...");
            TelegramBotsApi tgBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            tgBotsApi.registerBot(new CurrencyTelegramBot());

        } catch (TelegramApiRequestException e) {
            log.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
            throw new RuntimeException(e);
        }
        log.info("Telegram bot is ready to accept updates from user......");
    }
}
