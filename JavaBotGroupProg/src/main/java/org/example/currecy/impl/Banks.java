package org.example.currecy.impl;

public enum Banks {
    MONO("https://api.monobank.ua/bank/currency");

    final String url;

    Banks(String url) {
        this.url = url;
    }
}
