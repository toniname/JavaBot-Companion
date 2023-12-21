package org.example.currency.impl;

import java.io.IOException;

public interface CurrencyService {
    default double getSellRate(Currency ccy) throws IOException {
        return getRate(ccy);
    }
    default double getBuyRate(Currency ccy) throws IOException {
        return getRate(ccy);
    }
    double getRate(Currency ccy) throws IOException;

}
