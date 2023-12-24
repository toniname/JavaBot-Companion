package org.example.telegram;

import org.example.currency.impl.Banks;
import org.example.currency.impl.Currency;
import org.example.currency.impl.CurrencyServicesFacade;
import org.example.currency.sort.CurrencyRatePrettierImpl;
import org.example.telegram.command.*;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

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

        startNotificationsThread();
    }

    @Override
    public String getBotUsername() {
        return LoginAndToken.NAME;
    }

    @Override
    public String getBotToken() {
        return LoginAndToken.TOKEN;
    }

    public static synchronized Map<Long, SelectedOptions> getUsersOptions() {
        return usersOptions;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage())
            // Логіка обробки повідомлення
            processMessageUpdates(update);

        if (update.hasCallbackQuery()) {
            // Логіка обробки callback
            BotCommand command = processUpdatesWIthCallback(update);

            try {
                if (command != null) {
                    // Якщо команда не null, виконуємо її
                    usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).push(command);
                    command.execute(this,
                            update.getCallbackQuery().getMessage().getFrom(),
                            update.getCallbackQuery().getMessage().getChat(),
                            null
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong");
            }

            usersOptions.forEach((k, v) -> System.out.println(k + "  " + v));
        }
    }

    private void processMessageUpdates(Update update) {
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

    private BotCommand processUpdatesWIthCallback(Update update) {
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
            case "currency" -> command = new SelectCurrency();
            case "precision" -> command = new SelectPrecisoin();
            case "back" -> command = usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).pop();
            case "mono", "nbu", "pryvat" -> {
                usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setSelectedBank(callbackData);
                command = new SelectBank();
            }
            case "time" -> {
                command = new SelectTime();
                usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setEnableTimeSelection(true);
            }
            case "usd", "eur" -> {
                usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).setCurrency(callbackData);
                command = new SelectCurrency();
            }
            case "info" -> {
                sendRateMessage(update.getCallbackQuery().getMessage().getChatId());
                return null;
            }
        }

        return command;
    }

    private void sendRateMessage(long chatId) {
        SelectedOptions selectedOptions = usersOptions.get(chatId);
        String msg ;
        Currency selectedCurrency = Currency.valueOf(selectedOptions.getSelectedCurrency().toUpperCase());
        Banks selectedBank = Banks.valueOf(selectedOptions.getSelectedBank().toUpperCase());

        double rate;
        try {
            rate = currencyServicesFacade.getRate(selectedCurrency, selectedBank);
            msg = selectedBank + ":" + selectedCurrency + " rate is : " +
                    prettier.roundNum(rate, Integer.parseInt(selectedOptions.getPrecision()));
        } catch (IOException e) {
            msg = "Something went wrong";
            e.printStackTrace();
        }

        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.setText(msg);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void startNotificationsThread() {
        Thread thread = new Thread ( () -> {
            while (true) {
                Calendar rightNow = Calendar.getInstance();
                int hour = rightNow.get(Calendar.HOUR_OF_DAY);

                getUsersOptions().keySet().stream()
                        .filter((k) -> getUsersOptions().get(k).getTime() == hour)
                        .forEach(this::sendRateMessage);

                try {
                    Thread.sleep(1000 * 60 * 60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();
    }
}
