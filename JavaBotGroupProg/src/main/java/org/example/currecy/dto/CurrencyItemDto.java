package org.example.currecy.dto;

import lombok.Data;
import org.example.currecy.Currency;

@Data
public class CurrencyItemDto {

    private Currency ccy;
    private Currency base_ccy;
    private String buy;
    private String sale;

}