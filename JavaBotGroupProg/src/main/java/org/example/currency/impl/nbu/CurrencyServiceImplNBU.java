package org.example.currency.impl.nbu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.currency.impl.Currency;
import org.example.currency.dto.CurrencyItemDtoNBU;
import org.example.currency.impl.CurrencyService;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CurrencyServiceImplNBU implements CurrencyService {
    static String urlNBU = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json";
    List<CurrencyItemDtoNBU> allCurrencies;

    @Override
    public double getSaleRate(Currency ccy) throws IOException {
        CurrencyItemDtoNBU neededDto = getDtoObject(ccy);
        return neededDto.getRateSale();
    }

    @Override
    public double getBuyRate(Currency ccy) throws IOException {
        CurrencyItemDtoNBU neededDto = getDtoObject(ccy);
        return neededDto.getRate();
    }

    public CurrencyItemDtoNBU getDtoObject(Currency ccy) throws IOException {
        doRequest();
        CurrencyItemDtoNBU current = allCurrencies.stream()
                .filter((c) -> c.getCc() == ccy)
                .findFirst()
                .orElseThrow();
        System.out.println(current);
        return current;
    }

    private void doRequest() throws IOException {
        String jsonString = Jsoup.connect(urlNBU)
                .ignoreContentType(true)
                .get()
                .body()
                .text();

        System.out.println(jsonString);

        Type type = TypeToken
                .getParameterized(List.class, CurrencyItemDtoNBU.class)
                .getType();

        allCurrencies = new Gson().fromJson(jsonString, type);
    }

}
