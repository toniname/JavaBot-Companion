package org.example.currency.impl.pb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currency.dto.CurrencyItemDtoMONO;
import org.example.currency.dto.CurrencyItemDtoPB;
import org.example.currency.impl.Currency;
import org.example.currency.impl.CurrencyService;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CurrencyServiceImplPB implements CurrencyService {
    static String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
    List<CurrencyItemDtoPB> allCurrencies;

    @Override
    public double getSellRate(Currency ccy) throws IOException {
        CurrencyItemDtoPB neededDto = getDtoObject(ccy);

        return neededDto.getRateSell();
    }

    @Override
    public double getBuyRate(Currency ccy) throws IOException {
        CurrencyItemDtoPB neededDto = getDtoObject(ccy);
        return neededDto.getRateBuy();
    }

    @Override
    public double getRate(Currency ccy) throws IOException {
        return getBuyRate(ccy);
    }


    public CurrencyItemDtoPB getDtoObject(Currency ccy) throws IOException {
        doRequest();
        CurrencyItemDtoPB current = allCurrencies.stream()
                .filter((c) -> c.getCcy().equals(ccy))
                .filter((c) -> c.getBase_ccy() == Currency.UAH)
                .findFirst()
                .orElseThrow();
        System.out.println(current);

        return current;
    }

    private void doRequest() throws IOException {

        String jsonString = Jsoup.connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text();

        System.out.println(jsonString);

        Type type = TypeToken
                .getParameterized(List.class, CurrencyItemDtoPB.class)
                .getType();

        allCurrencies = new Gson().fromJson(jsonString, type);
    }
}
