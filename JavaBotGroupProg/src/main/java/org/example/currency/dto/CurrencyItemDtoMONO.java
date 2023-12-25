package org.example.currency.dto;


import lombok.Data;

@Data
public class CurrencyItemDtoMONO {
    private int currencyCodeA;
    private int currencyCodeB;
    private int date;
    private double rateBuy;
    private double rateSell;
}