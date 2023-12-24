package org.example.currency.impl;

public enum Banks {
    NBU, MONO, PRIVATE;

    public static Banks fromString(String bankString) {
        for (Banks bank : Banks.values()) {
            if (bank.name().equalsIgnoreCase(bankString)) {
                return bank;
            }
        }
        throw new IllegalArgumentException("Invalid bank: " + bankString);
    }
}