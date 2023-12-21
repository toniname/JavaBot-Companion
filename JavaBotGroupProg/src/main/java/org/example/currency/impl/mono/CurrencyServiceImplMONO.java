package org.example.currency.impl.mono;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currency.dto.CurrencyItemDtoMONO;
import org.example.currency.impl.Currency;
import org.example.currency.impl.CurrencyService;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;

import java.util.List;

public class CurrencyServiceImplMONO implements CurrencyService {

    static String url = "https://api.monobank.ua/bank/currency";
    List<CurrencyItemDtoMONO> allCurrencies;

    private final long cacheTime = 5 * 60; //time of using cached data in seconds
    private long lastRequestTime;

    @Override
    public double getSellRate(Currency ccy) throws IOException {
        CurrencyItemDtoMONO neededDto = getDtoObject(ccy);

        return neededDto.getRateSell();
    }

    @Override
    public double getBuyRate(Currency ccy) throws IOException {
        CurrencyItemDtoMONO neededDto = getDtoObject(ccy);
        return neededDto.getRateBuy();
    }

    @Override
    public double getRate(Currency ccy) throws IOException {
        return getBuyRate(ccy);
    }

    public CurrencyItemDtoMONO getDtoObject(Currency ccy) throws IOException {

        if ((System.currentTimeMillis() / 1000L) - lastRequestTime > cacheTime)
            doRequest();

        CurrencyItemDtoMONO current = allCurrencies.stream()
                .filter((c) -> c.getCurrencyCodeB() == Currency.UAH.getISOCode())
                .filter((c) -> c.getCurrencyCodeA() == ccy.getISOCode())
                .findFirst()
                .orElseThrow();
        System.out.println(current);

        return current;
    }

    private void doRequest() throws IOException {
        lastRequestTime = System.currentTimeMillis() / 1000L;

        String jsonString = Jsoup.connect(url)
                .ignoreContentType(true)
                .get()
                .body()
                .text();

        System.out.println(jsonString);

        Type type = TypeToken
                .getParameterized(List.class, CurrencyItemDtoMONO.class)
                .getType();

        allCurrencies = new Gson().fromJson(jsonString, type);
    }
}
