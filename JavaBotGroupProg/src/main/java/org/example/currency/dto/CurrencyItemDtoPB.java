package org.example.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.currency.impl.Currency;

@Data
@AllArgsConstructor
public class CurrencyItemDtoPB {
    private Currency ccy;
    private Currency base_ccy;
    private String buy;
    private String sale;
    public double rateBuy;
    public double rateSell;
}
