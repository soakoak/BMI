package com.irrotation.painoindeksi.client;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.irrotation.painoindeksi.shared.Laskuri;

public class LaskuriTest extends GWTTestCase {

	@Test
	public void testLaskePainoindeksi() {
		double pituus = 1.8;
		double paino = 80;
		assertEquals("pituus 1,8m, paino 80kg ", paino / ( pituus * pituus ),
				Laskuri.laskePainoindeksi(pituus, paino), 0.01 );
	}

	@Override
	public String getModuleName() {
		return "com.irrotation.painoindeksi.painoindeksi";
	}
	
	public void testPainoindeksiService() {
		PainoindeksiServiceAsync painoindeksiSvc = GWT.create(PainoindeksiService.class);
		ServiceDefTarget target = (ServiceDefTarget) painoindeksiSvc;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "painoindeksi/laske");
		
		// Since RPC calls are asynchronous, we will need to wait for a response
		// after this test method returns. This line tells the test runner to wait
		// up to 10 seconds before timing out.
		delayTestFinish(10000);
		
		// Send a request to the server.
		painoindeksiSvc.kerroIndeksi("25.86555", new AsyncCallback<String>() {
			
		  public void onFailure(Throwable caught) {
		    // The request resulted in an unexpected error.
		    fail("Request failure: " + caught.getMessage());
		  }
		
		  public void onSuccess(String result) {
		    // Verify that the response is correct.
		    assertTrue(result.startsWith("Painoindeksisi on 25,87"));
		
		    // Now that we have received a response, we need to tell the test runner
		    // that the test is complete. You must call finishTest() after an
		    // asynchronous test finishes successfully, or the test will time out.
		    finishTest();
		  }
		});
	}

}
