package org.example.telegram;

import org.example.currency.Currency;
import org.example.currency.CurrencyRatePrettier;
import org.example.currency.CurrencyService;
import org.example.currency.impl.CurrencyRatePrettierImpl;
import org.example.currency.impl.CurrencyServiceImpl;
import org.example.telegram.command.SelectBank;
import org.example.telegram.command.SelectPrecisoin;
import org.example.telegram.command.SettingsCommand;
import org.example.telegram.command.StartCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private final CurrencyService currencyService;
    private final CurrencyRatePrettier currencyRatePrettier;

    public CurrencyTelegramBot() {
        currencyService = new CurrencyServiceImpl();
        currencyRatePrettier = new CurrencyRatePrettierImpl();
        SelectedOptions.setDefault();
        register(new StartCommand());
        register(new SettingsCommand());
        register(new SelectBank());
        register(new SelectPrecisoin());

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
//            String receivedText = update.getMessage().getText();
//
//            SendMessage sm = new SendMessage();
//            sm.setText("You just wrote " + receivedText);
//            sm.setChatId(update.getMessage().getChatId());
//
//            try {
//                execute(sm);
//            } catch (TelegramApiException e) {
//                System.out.println("something went wrong");
//            }
        }


        if (update.hasCallbackQuery()) {
            BotCommand cm = null;
            String data = update.getCallbackQuery().getData();
            System.out.println(data);

            if (data.contains("setprecision")) {
                setPrecision(data);
                data = "precision";
            }

            switch (data) {
                case "settings" -> cm =  new SettingsCommand();
                case "bank" -> cm = new SelectBank();
                case "precision" -> cm = new SelectPrecisoin();

                case "mono", "nbu", "pryvat" -> {
                    SelectedOptions.setSelectedBank(data);
                    cm = new SelectBank();
                }

            }

            if (cm == null) return;
            try {
                cm.execute(this,
                        update.getCallbackQuery().getMessage().getFrom(),
                        update.getCallbackQuery().getMessage().getChat(),
                        null
                        );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong");
            }
        }
    }



    private void setPrecision(String s) {
        SelectedOptions.precision = s.substring(s.length()-1);
    }



    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRate(currency), currency);
    }
}
