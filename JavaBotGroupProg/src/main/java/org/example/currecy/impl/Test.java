package org.example.currecy.impl;

import org.example.currecy.Currency;
import org.example.currecy.sort.CurrencyRatePrettierImpl;

public class Test {
    public static void main(String[] args) {
        CurrencyServiceImplPB currencyService = new CurrencyServiceImplPB();
        double buyRate = currencyService.getRate(Currency.USD);
        double saleRate = currencyService.getRate(Currency.USD);
        CurrencyRatePrettierImpl crpi = new CurrencyRatePrettierImpl();
        String pretty = crpi.pretty(buyRate, saleRate, Currency.USD);
        System.out.println(pretty);


    }
}
