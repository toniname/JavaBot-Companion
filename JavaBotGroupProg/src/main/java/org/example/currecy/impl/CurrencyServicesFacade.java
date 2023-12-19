package org.example.currecy.impl;

import org.example.currecy.impl.mono.MonoCurrencyService;
import org.example.currecy.impl.nbu.CurrencyServiceImplNBU;

import java.io.IOException;

public class CurrencyServicesFacade {

    CurrencyService currencyService;


    /*TO DO*/
    private void setBank(Banks bank) {
        switch (bank) {
            case NBU -> currencyService = new CurrencyServiceImplNBU();
            case MONO -> currencyService = new MonoCurrencyService();
//            case PRYVAT -> ;
        }
    }

    public double getSellRate(Currency ccy, Banks bank) throws IOException {
        setBank(bank);
        return currencyService.getSellRate(ccy);
    }

    public double getBuyRate(Currency ccy, Banks bank) throws IOException {
        setBank(bank);
        return currencyService.getBuyRate(ccy);
    }

    public double getRate(Currency ccy, Banks bank) throws IOException {
        setBank(bank);
        return currencyService.getRate(ccy);
    }
}
