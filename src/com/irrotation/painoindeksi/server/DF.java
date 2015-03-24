package com.irrotation.painoindeksi.server;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DF {

	private static DecimalFormat df = null;
	
	private DF() {

	}
	
	public static DecimalFormat getInstance()
	{
		if( df == null)
		{
			df = new DecimalFormat("##0.00");
			df.setRoundingMode(RoundingMode.HALF_UP);
		}
		
		return df;
	}
}
