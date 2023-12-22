package org.example.telegram.userdata;

import lombok.Getter;
import org.glassfish.grizzly.utils.EchoFilter;

import java.util.HashMap;
import java.util.Map;

public class SelectedOptions {

    private Map<String, String> banks = new HashMap<>();
    private String bank = "NBU";
    private String precision = "2";
    private String currency = "usd";
    private String time = null;

    @Getter
    private boolean enableTimeSelection = false;

    public SelectedOptions() {
        setDefault();
    }


    public void setEnableTimeSelection(boolean enableTimeSelection) {
        this.enableTimeSelection = enableTimeSelection;
    }

    public void setDefault() {
        banks.put("mono", "✅");
        banks.put("pryvat", "");
        banks.put("nbu", "");
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


    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String isSelectedCurrency(String key) {
        if (currency.equals(key))
            return "✅";
        return "";
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