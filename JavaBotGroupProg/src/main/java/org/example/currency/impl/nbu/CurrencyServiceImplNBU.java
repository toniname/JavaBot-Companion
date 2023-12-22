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

    public double getRate(Currency cc) {
        String urlNBU = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json";

        String jsonStringNBU = "";

        try {

            jsonStringNBU = Jsoup.connect(urlNBU)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();

        } catch (IOException e) {
            System.out.println("Error while currency request!!!");
        }

        Type typeNBU = TypeToken.getParameterized(List.class, CurrencyItemDtoNBU.class)
                .getType();

        List<CurrencyItemDtoNBU> listNBU = new Gson().fromJson(jsonStringNBU, typeNBU);

        return listNBU.stream()
                .filter(c -> c.getCc() == cc)
                .map(CurrencyItemDtoNBU::getRate)
                .findFirst()
                .orElseThrow();
    }
}
