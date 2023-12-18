package org.example.currecy.dto;


import lombok.Builder;
import lombok.Data;
import org.example.currecy.CurrencyNBU;


@Data
@Builder
public class CurrencyItemDtoNBU {

	/*
{
   "r030":36,
   "txt":"Австралійський долар",
   "rate":24.8301,
   "cc":"AUD",
   "exchangedate":"18.12.2023"
}
	*/

	public int r030;
	public String txt;
	public double rate;
	public CurrencyNBU cc;
	public String exchangedate;
}
