package com.irrotation.painoindeksi.server;

import java.text.DecimalFormat;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.irrotation.painoindeksi.client.PainoindeksiService;
import com.irrotation.painoindeksi.shared.Laskuri;

@SuppressWarnings({"serial"})
public class PainoindeksiServiceImpl extends RemoteServiceServlet implements
		PainoindeksiService {

	@Override
	public String kerroIndeksi(String indeksi) {

		Double idx;

		try {
			idx = Double.parseDouble(indeksi);
		} catch (NumberFormatException e) {
			GWT.log("virhe parsiessa indeksi�");
			throw new IllegalArgumentException("Virheellinen indeksi");
		}

		return "Painoindeksisi on "
				+ DF.getInstance().format(idx);
	}

	@Override
	public String kerroIndeksi(Double paino, Double pituus, Integer ika) {

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		try {
			pm.makePersistent( new Henkilo(paino, pituus, ika));
		} finally {
			pm.close();
		}

		return kerroIndeksi("" + Laskuri.laskePainoindeksi(pituus, paino));
	}
	
	public Double getKeskiIka(List<Henkilo> l) 
	{
		Double n = 0.0, summa = 0.0;
		for( Henkilo hlo : l)
		{
			summa += hlo.getIka();
			n++;
		}
		
		if( n > 0) return summa / n ;
		else return 0.0;
	}
	
	public Double getKeskipituus(List<Henkilo> l) 
	{
		Double n = 0.0, summa = 0.0;
		for( Henkilo hlo : l)
		{
			summa += hlo.getPituus();
			n++;
		}
		
		if( n > 0) return summa / n ;
		else return 0.0;
	}
	
	public Double getKeskipaino(List<Henkilo> l) 
	{
		Double n = 0.0, summa = 0.0;
		for( Henkilo hlo : l)
		{
			summa += hlo.getPaino();
			n++;
		}
		
		if( n > 0) return summa / n ;
		else return 0.0;
	}
	
	public Double getKeskiIndeksi(List<Henkilo> l) 
	{
		Double n = 0.0, summa = 0.0;
		for( Henkilo hlo : l)
		{
			summa += Laskuri.laskePainoindeksi(hlo.getPituus(), hlo.getPaino());
			n++;
		}
		
		if( n > 0) return summa / n ;
		else return 0.0;
	}

	@Override
	public String[] getKeskiset() {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		
		Query q = pm.newQuery( Henkilo.class );
		String[] palautus = new String[4];
		DecimalFormat df = DF.getInstance();
		
		try{
			@SuppressWarnings("unchecked")
			List<Henkilo> hlot = (List<Henkilo>) q.execute();
			
			palautus[0] = df.format( getKeskiIka(hlot) );
			palautus[1] = df.format( getKeskipituus(hlot) );
			palautus[2] = df.format( getKeskipaino(hlot) );
			palautus[3] = df.format( getKeskiIndeksi(hlot) );
			
		} finally {
			q.closeAll();
			pm.close();
		}

		return palautus;
	}

	@Override
	public String[] getRajaKeskiset(int ala_ika, int yla_ika) {
		String[] palautus = new String[3];
		
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		Query q = pm.newQuery( Henkilo.class );
		q.setFilter("ika >= minAge && ika <= maxAge");
		q.declareParameters("int minAge, int maxAge");
		
		try {
			@SuppressWarnings("unchecked")
			List<Henkilo> hlot = (List<Henkilo>) q.execute( ala_ika, yla_ika );
			
			DecimalFormat df = DF.getInstance();
			palautus[0] = df.format( getKeskipituus(hlot) );
			palautus[1] = df.format( getKeskipaino(hlot) );
			palautus[2] = df.format( getKeskiIndeksi(hlot) );
		} finally {
			q.closeAll();
			pm.close();
		}

		return palautus;
	}
}
