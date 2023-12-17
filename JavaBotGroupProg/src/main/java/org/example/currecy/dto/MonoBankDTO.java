package org.example.currecy.dto;

import lombok.Data;

@Data
public class MonoBankDTO {
    public int currencyCodeA;
    public int currencyCodeB;
    public int date;
    public double rateBuy;
    public double rateSell;
}
