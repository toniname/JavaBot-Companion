package org.example.telegram.userdata;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static org.example.telegram.CurrencyTelegramBot.usersOptions;

public class SelectedOptions {

    private final Map<String, String> banks = new HashMap<>();
    private String precision = "2";
    private String currency = "USD";

    private String time = null;

    @Setter
    @Getter
    private boolean enableTimeSelection = false;

    public SelectedOptions() {
        setDefault();
    }

    public void setDefault() {
        banks.put("MONO", "✅");
        banks.put("PRYVAT", "");
        banks.put("NBU", "");
    }

    public void setSelectedBank(String key) {
        banks.replaceAll((k, v) -> "");
        banks.replace(key, "✅");
    }

    public boolean setTime(String timeToSet) {
        int intTime;

        try {
            intTime = Integer.parseInt(timeToSet);
        } catch (NumberFormatException e) {
            this.time = null;
            return false;
        }

        if (intTime >= 9 && intTime <= 18) {
            this.time = timeToSet;
            return true;
        } else {
            this.time = null;
            return false;
        }
    }

    public String isBankSelected(String key) {
        return banks.get(key);
    }

    public String isSelectedPrecision(String key) {
        if (precision.equals(key))
            return "✅";
        return "";
    }

    public void setPrecision(String precision, Update update) {
        this.precision = precision;

        // Получите chatId из объекта Update
        Long chatId = update.getMessage().getChatId();
        usersOptions.get(chatId).setPrecision("newPrecision", update);
    }

    public String isSelectedCurrency(String key) {
        if (currency.equals(key))
            return "✅";
        return "";
    }

    public String getSelectedBank() {
        for (Map.Entry<String, String> entry : banks.entrySet()) {
            if ("✅".equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getSelectedCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "SelectedOptions{" +
                "banks=" + banks +
                ", precision='" + precision + '\'' +
                ", currency='" + currency + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}