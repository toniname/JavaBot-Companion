package org.example.currency.impl;


import lombok.Getter;

@Getter
public enum Currency {
    UAH(980),
    EUR(978),
    USD(840);

    private final int ISOCode;

    Currency(int ISOCode) {
        this.ISOCode = ISOCode;
    }
}