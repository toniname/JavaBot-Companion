package org.example.telegram;

import org.example.currency.impl.Banks;
import org.example.currency.impl.Currency;
import org.example.currency.impl.CurrencyServicesFacade;
import org.example.currency.sort.CurrencyRatePrettier;
import org.example.telegram.command.*;
import org.example.telegram.userdata.LoginAndToken;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {
    private final Pattern commandPattern = Pattern.compile("/\\S+");
    public static final Map<Long, SelectedOptions> usersOptions = new HashMap<>();
    private static final CurrencyServicesFacade currencyServicesFacade = new CurrencyServicesFacade();
    private static final CurrencyRatePrettier prettier = new CurrencyRatePrettier();

    public CurrencyTelegramBot() {
        register(new HelpCommand());
        register(new StartCommand());
        register(new SettingsCommand());
        register(new SelectBank());
        register(new SelectPrecision());
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
            processMessageUpdates(update);

        if (update.hasCallbackQuery()) {
            BotCommand command = processUpdatesWIthCallback(update);

            try {
                if (command != null) {
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

        Long chatId = update.getMessage().getChatId();
        SelectedOptions options = usersOptions.getOrDefault(chatId, new SelectedOptions());

        if (options.isEnableTimeSelection()) {
            if (options.setTime(receivedText)) {
                sm.setText("Вибраний час: " + receivedText);
            } else {
                sm.setText("Вибраний час скасовано");
            }
        } else {
            sm.setText("Невідома команда \u2639");
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
                    .setPrecision(callbackData.substring(callbackData.length() - 1));
            callbackData = "precision";
        }

        switch (callbackData) {
            case "settings" -> command = new SettingsCommand();
            case "bank" -> command = new SelectBank();
            case "currency" -> command = new SelectCurrency();
            case "precision" -> command = new SelectPrecision();
            case "back" -> command = usersOptions.get(update.getCallbackQuery().getMessage().getChatId()).pop();
            case "mono", "nbu", "private" -> {
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
        System.out.println(callbackData);
        return command;
    }

    private void sendRateMessage(long chatId) {
        SelectedOptions selectedOptions = usersOptions.get(chatId);
        String msg;

        if (selectedOptions.getSelectedCurrency().isEmpty()) {
            SendMessage sm = new SendMessage();
            msg = "Будь ласка виберіть валюту";
            sm.setChatId(chatId);
            sm.setText(msg);
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            return;
        }

        String[] selectedCurrencies = selectedOptions.getSelectedCurrency().toArray(String[]::new);
        Banks selectedBank;

        for (String currency : selectedCurrencies) {
            Currency selectedCurrency = Currency.valueOf(currency.toUpperCase());
            selectedBank = Banks.valueOf(selectedOptions.getSelectedBank().toUpperCase());

            double rateBuy;
            double rateSale;
            try {
                rateBuy = currencyServicesFacade.getBuyRate(selectedCurrency, selectedBank);
                rateSale = currencyServicesFacade.getSaleRate(selectedCurrency, selectedBank);

                msg = "Обраний банк " + selectedBank.name() + ", \nКурс " + selectedCurrency + ":\nКупівля: " +
                        prettier.roundNum(rateBuy, Integer.parseInt(selectedOptions.getPrecision())) + "; Продаж: " + prettier.roundNum(rateSale, Integer.parseInt(selectedOptions.getPrecision()));
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
    }

    private void commandHelp(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            if (!commandPattern.matcher(message).matches()) {
                SendMessage responseMessage = new SendMessage();
                responseMessage.setText("""
                        Ви ввели текст який бот не може розпізнати🤷🏼‍♂️
                        Цей бот знає ось такі команди:\s
                        /start ~ /help""");
                responseMessage.setChatId(update.getMessage().getChatId());
                execute(responseMessage);
            } else if ("/help".equals(message)) {
                HelpCommand helpCommand = new HelpCommand();
                helpCommand.execute(this, update.getMessage().getFrom(), update.getMessage().getChat(), null);
            }
        }
    }

    private void startNotificationsThread() {
        Thread thread = new Thread(() -> {
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