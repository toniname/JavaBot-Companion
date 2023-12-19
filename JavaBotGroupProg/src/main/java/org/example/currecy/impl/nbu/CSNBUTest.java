package org.example.currecy.impl.nbu;


import org.example.currecy.impl.Currency;


public class CSNBUTest {
	public static void main(String[] args) {
		CurrencyServiceImplNBU csNBU = new CurrencyServiceImplNBU();
		System.out.println(csNBU.getRate(Currency.USD));
	}
}
