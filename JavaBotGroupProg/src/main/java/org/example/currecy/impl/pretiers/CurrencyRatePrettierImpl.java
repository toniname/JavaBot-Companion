package org.example.currecy.impl.pretiers;

import org.example.currecy.impl.Currency;

public class CurrencyRatePrettierImpl implements CurrencyRatePrettier {

    @Override
    public String pretty(double rate, Currency ccy, int precision) {
        return String.format("%."+precision +"f", rate);

    }
}
