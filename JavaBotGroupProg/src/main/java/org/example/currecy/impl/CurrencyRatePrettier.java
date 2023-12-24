package org.example.currecy.impl;

public interface CurrencyRatePrettier {
    String pretty(double buyRate, double saleRate, Currency ccy);
}