package org.example.currency.dto;

import lombok.Data;

@Data
public class CurrencyItemDtoMONO {
    public int currencyCodeA;
    public int currencyCodeB;
    public int date;
    public double rateBuy;
    public double rateSell;
}
