package com.irrotation.painoindeksi.shared;

public class Laskuri {
	
	public static Double laskePainoindeksi(double pituus, double paino)
	{
		return paino / ( pituus * pituus );
	}
}
