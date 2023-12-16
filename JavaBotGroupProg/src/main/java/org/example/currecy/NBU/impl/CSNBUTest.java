package org.example.currecy.NBU.impl;


import static org.example.currecy.NBU.CurrencyNBU.XAU;

public class CSNBUTest {
	public static void main(String[] args) {
		CurrencyServiceImplNBU csNBU = new CurrencyServiceImplNBU();
		System.out.println(csNBU.getRate(XAU));
	}
}
