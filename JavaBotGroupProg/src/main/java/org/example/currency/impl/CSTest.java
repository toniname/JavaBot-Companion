package org.example.currency.impl;

import org.example.currency.Currency;

public class CSTest {

    public static void main(String[] args) {
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
        System.out.println("currencyService.getRate(Currency.USD) = " + currencyService.getRate(Currency.USD));
    }
}
