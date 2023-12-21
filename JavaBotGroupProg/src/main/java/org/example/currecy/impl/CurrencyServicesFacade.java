package org.example.currecy.impl;

import org.example.currecy.impl.mono.CurrencyServiceImplMONO;
import org.example.currecy.impl.nbu.CurrencyServiceImplNBU;
import org.example.currecy.impl.pb.CurrencyServiceImplPB;

import java.io.IOException;

public class CurrencyServicesFacade {

    CurrencyService currencyService;


    /*TO DO*/
    private void setBank(Banks bank) {
        switch (bank) {
            case NBU -> currencyService = new CurrencyServiceImplNBU();
            case MONO -> currencyService = new CurrencyServiceImplMONO();
            case PRYVAT -> currencyService =  new CurrencyServiceImplPB();
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
