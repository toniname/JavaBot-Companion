package org.example.telegram;

import lombok.Getter;
import org.example.currency.CurrencyRatePrettier;
import org.example.currency.impl.CurrencyService;
import org.example.currency.sort.CurrencyRatePrettierImpl;
import org.example.currency.impl.Currency;
import org.example.currency.impl.mono.CurrencyServiceImplMONO;
import org.example.currency.impl.nbu.CurrencyServiceImplNBU;
import org.example.currency.impl.pb.CurrencyServiceImplPB;
import org.example.telegram.command.*;
import org.example.telegram.userdata.SelectedOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private final CurrencyService currencyService;
    private final CurrencyRatePrettier currencyRatePrettier;

    @Getter
    private static final Map<Long, SelectedOptions> usersOptions = new HashMap<>();

    public CurrencyTelegramBot() {
        currencyService = new CurrencyServiceImplMONO(); // Выберите реализацию по умолчанию
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
    public String getBotToken() {
        return LoginAndToken.TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleIncomingMessage(Message message) {
        // Обработка входящего текстового сообщения, если необходимо
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        BotCommand command = null;
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        if (!usersOptions.containsKey(chatId) || usersOptions.get(chatId) == null) {
            usersOptions.put(chatId, new SelectedOptions());
        }

        if (callbackData.contains("setPrecision")) {
            setPrecision(callbackData, chatId);
            callbackData = "precision";
        }

        switch (callbackData) {
            case "settings" -> command = new SettingsCommand();
            case "bank" -> command = new SelectBank();
            case "usd", "eur" -> {
                setCurrency(callbackData, chatId);
                command = new SelectCurrency();
            }
            case "currency" -> command = new SelectCurrency();
            case "precision" -> command = new SelectPrecisoin();
            case "mono", "nbu", "pryvat" -> {
                setSelectedBank(callbackData, chatId);
                command = new SelectBank();
            }
            case "time" -> {
                command = new SelectTime();
                setEnableTimeSelection(chatId, true);
            }
        }

        try {
            if (command != null) {
                command.execute(this, callbackQuery.getFrom(), callbackQuery.getMessage().getChat(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
    }

    private void setPrecision(String data, long chatId) {
        SelectedOptions options = usersOptions.get(chatId);
        if (options != null) {
            options.setPrecision(data.substring(data.length() - 1));
        }
    }

    private void setCurrency(String data, long chatId) {
        SelectedOptions options = usersOptions.get(chatId);
        if (options != null) {
            options.setCurrency(data);
        }
    }

    private void setSelectedBank(String data, long chatId) {
        SelectedOptions options = usersOptions.get(chatId);
        if (options != null) {
            options.setSelectedBank(data);
        }
    }

    private void setEnableTimeSelection(long chatId, boolean enableTimeSelection) {
        SelectedOptions options = usersOptions.get(chatId);
        if (options != null) {
            options.setEnableTimeSelection(enableTimeSelection);
        }
    }
}
