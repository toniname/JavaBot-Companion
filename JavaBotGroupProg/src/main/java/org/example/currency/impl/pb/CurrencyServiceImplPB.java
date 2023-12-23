package org.example.currency.impl.pb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currency.impl.Currency;
import org.example.currency.dto.CurrencyItemDtoPB;
import org.example.currency.impl.CurrencyService;
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
                .filter(c -> c.getCcy().equals(ccy))
                .filter(c -> c.getBase_ccy() == Currency.UAH)
                .map(CurrencyItemDtoPB::getBuy)
                .findFirst()
                .orElseThrow();
        return Double.parseDouble(s);
    }


}
