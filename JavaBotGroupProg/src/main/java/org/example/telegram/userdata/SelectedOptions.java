package org.example.telegram.userdata;

import lombok.Getter;
import lombok.Setter;
import org.example.telegram.command.StartCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SelectedOptions {

    private final Map<String, String> banks = new HashMap<>();

    @Getter
    private final Stack<BotCommand> history = new Stack<>();

    @Getter
    private String precision = "2";
    private String currency = "usd";



    private String time = null;

    @Setter
    @Getter
    private boolean enableTimeSelection = false;

    public SelectedOptions() {
        setDefault();
    }



    public void push(BotCommand command) {

        if (!history.isEmpty() && history.peek().getClass().equals(command.getClass()))
            return;

        history.push(command);
    }

    public BotCommand pop() {
        history.pop();
        if (!history.isEmpty())
            return history.pop();
        return new StartCommand();
    }


    public void setDefault() {
        banks.put("mono", "✅");
        banks.put("private", "");
        banks.put("nbu", "");
    }

    public void setSelectedBank(String key) {
        banks.replaceAll((k, v) -> "");
        banks.replace(key, "✅");
    }

    public synchronized boolean setTime(String timeToSet) {
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

    public synchronized int getTime() {
        if (time == null) return -1;
        return Integer.parseInt(time);
    }

    public String isBankSelected(String key) {
        return banks.get(key);
    }

    public String isSelectedPrecision(String key) {
        if (this.precision.equals(key))
            return "✅";
        return "";
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String isSelectedCurrency(String key) {
        if (currency.equals(key))
            return "✅";
        return "";
    }

    public String getSelectedBank() {
        for (String key : banks.keySet()) {
            if ("✅".equals(banks.get(key))) {
                return key;
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