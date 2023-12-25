package org.example.currency.sort;

public class CurrencyRatePrettier {

    public String roundNum(double value, int precision) {
        String format = "%." + precision + "f";
        return String.format(format, value);
    }
}