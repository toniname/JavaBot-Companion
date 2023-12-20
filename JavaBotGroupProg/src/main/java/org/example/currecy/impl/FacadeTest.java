package org.example.currecy.impl;

import org.example.currecy.impl.Banks;
import org.example.currecy.impl.CurrencyServicesFacade;

import java.io.IOException;

public class FacadeTest {


    public static void main(String[] args) {
        CurrencyServicesFacade service = new CurrencyServicesFacade();
        try {
            System.out.println(service.getRate(Currency.USD, Banks.MONO));
            System.out.println(service.getRate(Currency.USD, Banks.NBU));
            System.out.println(service.getRate(Currency.USD, Banks.PRYVAT));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
