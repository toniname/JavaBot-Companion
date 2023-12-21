package org.example.telegram;

import lombok.Getter;
import org.example.currency.impl.Currency;
import org.example.currency.CurrencyRatePrettier;
import org.example.currency.CurrencyService;
import org.example.currency.sort.CurrencyRatePrettierImpl;
import org.example.currency.impl.mono.CurrencyServiceImplMONO;
import org.example.currency.impl.nbu.CurrencyServiceImplNBU;
import org.example.currency.impl.pb.CurrencyServiceImplPB;
import org.example.telegram.command.*;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private final CurrencyService currencyService;
    private final CurrencyRatePrettier currencyRatePrettier;


    @Getter
    private static final Map<Long, SelectedOptions> usersOptions = new HashMap<>();


    public CurrencyTelegramBot() {
        currencyService = new CurrencyServiceImpl();
        currencyRatePrettier = new CurrencyRatePrettierImpl();
        register(new StartCommand());
        register(new SettingsCommand());
        register(new SelectBank());
        register(new SelectPrecisoin());
        register(new SelectCurrency());
        register(new SelectTime());
    }

    @Override
    public String getBotUsername() {
        return LoginAndToken.NAME;
    }

    @Override
    public String getBotToken() { return LoginAndToken.TOKEN; }



    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            String receivedText = update.getMessage().getText();
            SendMessage sm = new SendMessage();
            sm.setChatId(update.getMessage().getChatId());

            /*case when user changes time*/
            if (usersOptions.get(update.getMessage().getChatId()).isEnableTimeSelection()) {
                if (usersOptions.get(update.getMessage().getChatId()).setTime(receivedText))
                    sm.setText("Time of notifications is set to: " + receivedText);
                else
                    sm.setText("Time of notifications is disabled");
            }

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                System.out.println("something went wrong");
            }
        }

        if (update.hasCallbackQuery()) {
            BotCommand command = null;
            usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setEnableTimeSelection(false);

            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.contains("setprecision")) {
                setPrecision(callbackData, update);
                callbackData = "precision";
            }

            switch (callbackData) {
                case "settings" -> command =  new SettingsCommand();
                case "bank" -> command = new SelectBank();
                case "usd" , "eur" ->
                {
                    setCurrency(callbackData,update);
                    command = new SelectCurrency();
                }

                case "currency" -> command = new SelectCurrency();
                case "precision" -> command = new SelectPrecisoin();
                case "mono", "nbu", "pryvat" -> {
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setSelectedBank(callbackData);
                    command = new SelectBank();
                }
                case "time" ->
                {
                    command = new SelectTime();
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setEnableTimeSelection(true);
                }
            }

            try {
                if (command == null) return;
                command.execute(this,
                        update.getCallbackQuery().getMessage().getFrom(),
                        update.getCallbackQuery().getMessage().getChat(),
                        null
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong");
            }
        }

        usersOptions.forEach((k, v) -> System.out.println(k + "  " +  v));

    }

    private void setPrecision(String s, Update update) {
        usersOptions.get(update.getCallbackQuery().getMessage().getChatId())
                .setPrecision(s.substring(s.length()-1));
    }
    private void setCurrency(String s, Update update) {
        usersOptions.get(update.getCallbackQuery().getMessage().getChatId())
                .setCurrency(s);
    }

    private String getRate(String ccy) {
        Currency currency = Currency.valueOf(ccy);
        return currencyRatePrettier.pretty(currencyService.getRate(currency), currency);
    }
}