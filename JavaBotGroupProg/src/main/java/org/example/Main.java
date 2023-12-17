package org.example;

import org.example.currecy.impl.Currency;
import org.example.currecy.impl.CurrencyRatePrettier;
import org.example.currecy.impl.CurrencyRatePrettierImpl;
import org.example.currecy.impl.CurrencyService;
import org.example.currecy.impl.mono.MonoCurrencyService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        CurrencyService currencyService = new MonoCurrencyService();

        CurrencyRatePrettierImpl c = new CurrencyRatePrettierImpl();

        System.out.println(c.pretty(3.12345678, Currency.UAH, 10));

//        try {
//            System.out.println(currencyService.getBuyRate(Currency.USD));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
