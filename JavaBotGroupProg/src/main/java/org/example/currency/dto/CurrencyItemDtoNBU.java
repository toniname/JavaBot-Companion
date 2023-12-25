package org.example.currency.dto;

import lombok.Builder;
import lombok.Data;
import org.example.currency.impl.Currency;


@Data
@Builder
public class CurrencyItemDtoNBU {
    private int r030;
    private String txt;
    private double rate;
    private double rateSale;
    private Currency cc;
    private String exchangedate;
}