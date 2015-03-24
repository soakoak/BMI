package com.irrotation.painoindeksi.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
	
	private static PersistenceManagerFactory pmfInstance = null;
	
	private PMF() {
		
	}
	
	public static PersistenceManagerFactory getInstance() {
		
		if(pmfInstance == null) {
			pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		}
		
		return pmfInstance;
	}

}
