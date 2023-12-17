package org.example.currecy.impl;

import java.io.IOException;

public interface CurrencyService {
    double getSellRate(Currency ccy) throws IOException;
    double getBuyRate(Currency ccy) throws IOException;

}
