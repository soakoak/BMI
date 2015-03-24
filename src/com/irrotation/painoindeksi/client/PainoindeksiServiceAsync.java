package com.irrotation.painoindeksi.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PainoindeksiServiceAsync {

	void kerroIndeksi(String indeksi,
			AsyncCallback<String> callback);
	
	void kerroIndeksi(Double paino, Double pituus, Integer ika,
			AsyncCallback<String> callback);

	void getKeskiset(AsyncCallback<String[]> callback);

	void getRajaKeskiset(int ala_ika, int yla_ika,
			AsyncCallback<String[]> callback);

}
