package org.example.currecy.impl;

import org.example.currecy.Currency;

public class CRPTest {

    public static void main(String[] args) {
        CurrencyServiceImpl csi = new CurrencyServiceImpl();
        double rate = csi.getRate(Currency.USD);

        CurrencyRatePrettierImpl crpi = new CurrencyRatePrettierImpl();
        String pretty = crpi.pretty(rate, Currency.USD);
        System.out.println(pretty);
    }

}
