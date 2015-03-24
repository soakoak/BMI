package com.irrotation.painoindeksi.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Henkilo {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Key key;
	
	@Persistent
	private Double paino;
	
	@Persistent
	private Double pituus;
	
	@Persistent
	private Integer ika;
	
	public Henkilo() {
		
	}
	
	public Henkilo(Double paino, Double pituus, Integer ika){
		this.paino = paino;
		this.pituus = pituus;
		this.ika = ika;
	}

	public Double getPaino() {
		return paino;
	}

	public void setPaino(Double paino) {
		this.paino = paino;
	}

	public Double getPituus() {
		return pituus;
	}

	public void setPituus(Double pituus) {
		this.pituus = pituus;
	}

	public Integer getIka() {
		return ika;
	}

	public void setIka(Integer ika) {
		this.ika = ika;
	}

	public Key getKey() {
		return key;
	}

}
