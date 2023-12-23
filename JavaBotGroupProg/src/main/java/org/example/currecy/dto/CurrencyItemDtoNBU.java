package org.example.currecy.dto;

import lombok.Builder;
import lombok.Data;
import org.example.currecy.impl.Currency;

@Data
@Builder
public class CurrencyItemDtoNBU {

	public int r030;
	public String txt;
	public double rate;
	public Currency cc;
	public String exchangedate;
}
