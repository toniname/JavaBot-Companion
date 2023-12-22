package org.example.currency;

import org.example.currency.impl.Currency;

public interface CurrencyRatePrettier {
    String pretty(double buyRate, double saleRate, Currency ccy);
}
