package org.example.telegram;

import lombok.Getter;
import org.example.currency.impl.Banks;
import org.example.currency.impl.Currency;
import org.example.currency.impl.CurrencyServicesFacade;
import org.example.currency.impl.mono.CurrencyServiceImplMONO;
import org.example.currency.impl.nbu.CurrencyServiceImplNBU;
import org.example.currency.impl.pb.CurrencyServiceImplPB;
import org.example.currency.impl.CurrencyService;
import org.example.currency.sort.CurrencyRatePrettierImpl;
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


    @Getter
    public static final Map<Long, SelectedOptions> usersOptions = new HashMap<>();


    private static final CurrencyServicesFacade currencyServicesFacade = new CurrencyServicesFacade();
    private static final CurrencyRatePrettierImpl prettier = new CurrencyRatePrettierImpl();

    public CurrencyTelegramBot() {

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
    public String getBotToken() {
        return LoginAndToken.TOKEN;
    }


    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            // Логіка обробки повідомлення

            String receivedText = update.getMessage().getText();
            SendMessage sm = new SendMessage();
            sm.setChatId(update.getMessage().getChatId());

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
            // Логіка обробки callback

            BotCommand command = null;
            usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setEnableTimeSelection(false);

            String callbackData = update.getCallbackQuery().getData();

            if (callbackData.contains("setprecision")) {
                usersOptions.get(update.getCallbackQuery().getMessage().getChatId())
                        .setPrecision(callbackData.substring(callbackData.length()-1));

                callbackData = "precision";
            }

            switch (callbackData) {
                case "settings" -> command = new SettingsCommand();
                case "bank" -> command = new SelectBank();
                case "back" -> {
                    System.out.println(usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).getHistory());

                    command = usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).pop();
                }
                case "currency" -> command = new SelectCurrency();
                case "precision" -> command = new SelectPrecisoin();
                case "mono", "nbu", "pryvat" -> {
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setSelectedBank(callbackData);
                    command = new SelectBank();
                }
                case "time" -> {
                    command = new SelectTime();
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setEnableTimeSelection(true);
                }
                case "usd", "eur" -> {
                    setCurrency(callbackData, update);
                    command = new SelectCurrency();
                }
            }

            try {
                if (command != null) {
                    // Якщо команда не null, виконуємо її
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).push(command);
                    command.execute(this,
                            update.getCallbackQuery().getMessage().getFrom(),
                            update.getCallbackQuery().getMessage().getChat(),
                            null
                    );
                } else if (callbackData.equalsIgnoreCase("info")) {
                    // В іншому випадку отримуємо курс валюти
                    CurrencyService currencyService = getCurrencyServiceForSelectedBank(update.getCallbackQuery().getMessage().getChatId());
                    SelectedOptions selectedOptions = usersOptions.get(update.getCallbackQuery().getMessage().getChatId());

                    Currency selectedCurrency = Currency.valueOf(selectedOptions.getSelectedCurrency().toUpperCase());
                    Banks selectedBank = Banks.valueOf(selectedOptions.getSelectedBank().toUpperCase());
                    double rate = currencyServicesFacade.getRate(selectedCurrency, selectedBank);

                    String msg = selectedBank + ":" + selectedCurrency + "rate is : " +
                            prettier.roundNum(rate, Integer.parseInt(selectedOptions.getPrecision()));

                    SendMessage sm = new SendMessage();
                    sm.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    sm.setText(msg);
                    execute(sm);


//                    if (selectedCurrencyValue != null) {
//                        Currency selectedCurrency = Currency.valueOf(selectedCurrencyValue);
//                        double rate;
//                        try {
//                            assert currencyService != null;
//                            rate = currencyService.getRate(selectedCurrency);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        // Виводимо курс валюти або використовуємо rate за необхідності
//                        SendMessage sm = new SendMessage();
//                        sm.setChatId(update.getCallbackQuery().getMessage().getChatId());
//                        sm.setText("Exchange rate for " + selectedCurrency + ": " + rate);
//
//                        execute(sm);
//                    } else {
//                        // Обработка ситуации, когда значение не установлено
//                        SendMessage sm = new SendMessage();
//                        sm.setChatId(update.getCallbackQuery().getMessage().getChatId());
//                        sm.setText("Selected currency is not set");
//                        execute(sm);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong");
            }

            usersOptions.forEach((k, v) -> System.out.println(k + "  " + v));
        }
    }

    private String getSelectedCurrency(Long chatId) {
        return usersOptions.get(chatId).getSelectedCurrency();
    }

    private CurrencyService getCurrencyServiceForSelectedBank(Long chatId) {
        String selectedBankValue = usersOptions.get(chatId).getSelectedBank();

        if (selectedBankValue != null) {
            try {
                Banks selectedBank = Banks.fromString(selectedBankValue);

                return switch (selectedBank) {
                    case NBU -> new CurrencyServiceImplNBU();
                    case MONO -> new CurrencyServiceImplMONO();
                    case PRYVAT -> new CurrencyServiceImplPB();
                };
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void setCurrency(String callbackData, Update update) {
        usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setCurrency(callbackData);
    }


}
