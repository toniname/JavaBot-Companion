package org.example.telegram;

import org.example.currency.Currency;
import org.example.currency.CurrencyRatePrettier;
import org.example.currency.CurrencyService;
import org.example.currency.impl.CurrencyRatePrettierImpl;
import org.example.currency.impl.CurrencyServiceImpl;
import org.example.telegram.command.StartCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private final CurrencyService currencyService;
    private final CurrencyRatePrettier currencyRatePrettier;

    public CurrencyTelegramBot() {
        currencyService = new CurrencyServiceImpl();
        currencyRatePrettier = new CurrencyRatePrettierImpl();
        register(new StartCommand());
    }

    @Override
    public String getBotUsername() {

        return LoginAndToken.NAME;
    }

    @Override
    public String getBotToken() {
        return LoginAndToken.TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            String receivedText = update.getMessage().getText();

            SendMessage sm = new SendMessage();
            sm.setText("You just wrote " + receivedText);
            sm.setChatId(update.getMessage().getChatId());

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                System.out.println("something went wrong");
            }
        }

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String prettyRate = getRate(data);

            SendMessage sm = new SendMessage();
            sm.setText(prettyRate);
            sm.setChatId(update.getCallbackQuery().getMessage().getChatId());

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                System.out.println("something went wrong");
            }
        }
    }

    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRate(currency), currency);
    }
}
