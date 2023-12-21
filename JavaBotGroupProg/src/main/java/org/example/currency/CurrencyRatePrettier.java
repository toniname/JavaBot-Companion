package org.example.currency;

public interface CurrencyRatePrettier {
    String pretty(double buyRate, double saleRate, Currency ccy);
}
