package org.example.currency.dto;

import lombok.Data;
import org.example.currency.Currency;

@Data
public class CurrencyItemDto {

    private Currency ccy;
    private Currency base_ccy;
    private String buy;
    private String sale;

}