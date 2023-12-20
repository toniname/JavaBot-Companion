package org.example.currecy.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currecy.Currency;
import org.example.currecy.CurrencyService;
import org.example.currecy.dto.CurrencyItemDtoPB;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CurrencyServiceImplPB implements CurrencyService {
    @Override
    public double getRate(Currency ccy) {
        String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
        String text = "";
        try {
            text = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            System.out.println("Error while currency request");
        }
        Type type = TypeToken.getParameterized(List.class, CurrencyItemDtoPB.class)
                .getType();
        List<CurrencyItemDtoPB> list = new Gson().fromJson(text, type);

        String s = list.stream()
                .filter(c -> c.getCcy() == ccy)
                .filter(c -> c.getBase_ccy() == Currency.UAH)
                .map(c -> c.getBuy())
                .findFirst()
                .orElseThrow();
        return Double.valueOf(s);
    }
}
