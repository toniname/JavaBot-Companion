package org.example.currency.impl;

import org.example.currency.impl.mono.CurrencyServiceImplMONO;
import org.example.currency.impl.nbu.CurrencyServiceImplNBU;
import org.example.currency.impl.pb.CurrencyServiceImplPB;

import java.io.IOException;

public class CurrencyServicesFacade {

    CurrencyService currencyService;

    private void setBank(Banks bank) {
        switch (bank) {
            case NBU -> currencyService = new CurrencyServiceImplNBU();
            case MONO -> currencyService = new CurrencyServiceImplMONO();
            case PRIVATE -> currencyService =  new CurrencyServiceImplPB();
        }
    }

    public double getSaleRate(Currency ccy, Banks bank) throws IOException {
        setBank(bank);
        return currencyService.getSaleRate(ccy);
    }

    public double getBuyRate(Currency ccy, Banks bank) throws IOException {
        setBank(bank);
        return currencyService.getBuyRate(ccy);
    }

   // public double getRate(Currency ccy, Banks bank) throws IOException {
     //   setBank(bank);
       // return currencyService.getRate(ccy);
    //}
}