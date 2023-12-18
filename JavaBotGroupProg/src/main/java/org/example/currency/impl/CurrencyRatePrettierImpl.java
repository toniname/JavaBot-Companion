package org.example.currency.impl;

import org.example.currency.Currency;
import org.example.currency.CurrencyRatePrettier;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyRatePrettierImpl implements CurrencyRatePrettier {
    public static final String FORMAT = "Курс %s => UAH = %s";

    @Override
    public String pretty(double rate, Currency ccy) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return String.format(FORMAT, ccy, df.format(rate));
    }
}
