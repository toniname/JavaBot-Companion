package org.example.currecy.impl.pretiers;

import org.example.currecy.impl.Currency;

public interface CurrencyRatePrettier {
    String pretty(double rate, Currency ccy, int precision);

}
