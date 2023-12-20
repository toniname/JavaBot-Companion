package org.example.currecy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.currecy.Currency;

@Data
@AllArgsConstructor
public class CurrencyItemDtoPB {
    private Currency ccy;
    private Currency base_ccy;
    private String buy;
    private String sale;
}
