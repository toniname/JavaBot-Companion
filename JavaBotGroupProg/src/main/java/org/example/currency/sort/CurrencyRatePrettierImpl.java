package org.example.currency.sort;


import org.example.currency.CurrencyRatePrettier;
import org.example.currency.impl.Currency;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyRatePrettierImpl implements CurrencyRatePrettier {
    public static final String FORMAT = "Курс %s => UAH: Покупка - %s, Продаж - %s";

    @Override
    public String pretty(double buyRate, double saleRate, Currency ccy) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return String.format(FORMAT, ccy, df.format(buyRate), df.format(saleRate));
    }

    public String roundNum(double value, int precision) {
        String format = "%." + precision + "f";
        return String.format(format, value);

    }
}