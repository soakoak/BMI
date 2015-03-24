package com.irrotation.painoindeksi.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("laske")
public interface PainoindeksiService extends RemoteService {
	
	String kerroIndeksi(String indeksi);
	
	String kerroIndeksi(Double paino, Double pituus, Integer ika);
	
	String[] getKeskiset();
	
	String[] getRajaKeskiset(int ala_ika, int yla_ika);
}
