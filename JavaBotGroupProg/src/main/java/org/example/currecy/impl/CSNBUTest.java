package org.example.currecy.impl;


import static org.example.currecy.CurrencyNBU.XAU;

public class CSNBUTest {
	public static void main(String[] args) {
		CurrencyServiceImplNBU csNBU = new CurrencyServiceImplNBU();
		System.out.println(csNBU.getRate(XAU));
	}
}
