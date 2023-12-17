package org.example.currecy.impl;

import lombok.Getter;

public enum Currency {
    UAH(980),
    EUR(978),
    USD(840);

    @Getter
    private final int ISOCode;

    Currency(int ISOCode) {
        this.ISOCode = ISOCode;
    }
}
