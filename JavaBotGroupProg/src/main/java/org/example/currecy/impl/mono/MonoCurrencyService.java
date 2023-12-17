package org.example.currecy.impl.mono;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currecy.dto.MonoBankDTO;
import org.example.currecy.impl.Currency;
import org.example.currecy.impl.CurrencyService;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;

import java.util.List;

public class MonoCurrencyService implements CurrencyService {

    static String url = "https://api.monobank.ua/bank/currency";
    List<MonoBankDTO> allCurrencies;

    private final long cacheTime = 5 * 60; //time of using cached data in seconds
    private long lastRequestTime;

    @Override
    public double getSellRate(Currency ccy) throws IOException {
        MonoBankDTO neededDto = getDtoObject(ccy);

        return neededDto.getRateSell();
    }

    @Override
    public double getBuyRate(Currency ccy) throws IOException {
        MonoBankDTO neededDto = getDtoObject(ccy);
        return neededDto.getRateBuy();
    }

    public MonoBankDTO getDtoObject(Currency ccy) throws IOException {

        if ((System.currentTimeMillis() / 1000L) - lastRequestTime > cacheTime)
            doRequest();

        MonoBankDTO current = allCurrencies.stream()
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
                .getParameterized(List.class, MonoBankDTO.class)
                .getType();

        allCurrencies = new Gson().fromJson(jsonString, type);
    }
}
