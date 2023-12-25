package org.example.currency.impl;

import java.io.IOException;

public interface CurrencyService {
    double getSaleRate(Currency ccy) throws IOException;
    double getBuyRate(Currency ccy) throws IOException;
}