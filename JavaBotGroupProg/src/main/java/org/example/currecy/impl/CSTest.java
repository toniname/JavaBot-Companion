package org.example.currecy.impl;

import org.example.currecy.Currency;

public class CSTest {

    public static void main(String[] args) {
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
        System.out.println("currencyService.getRate(Currency.USD) = " + currencyService.getRate(Currency.USD));
    }
}
