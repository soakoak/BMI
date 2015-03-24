package com.irrotation.painoindeksi.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Painoindeksi implements EntryPoint {

	private final PainoindeksiServiceAsync indeksiSvc = GWT
			.create(PainoindeksiService.class);

	private List<TextBox> vnshBoxes = new ArrayList<TextBox>();

	private final ClickHandler txtVanisher = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {

			for (TextBox box : vnshBoxes) {
				if (event.getRelativeElement().getId()
						.equals(box.getElement().getId())) {
					box.setText("");
					break;
				}
			}
		}
	};

	private Element getElement(String id) {
		return DOM.getElementById(id);
	}

	private void makeVanishable(TextBox box) {
		vnshBoxes.add(box);
		box.addClickHandler(txtVanisher);
	}

	public void onModuleLoad() {

		final Button calc = Button.wrap(DOM.getElementById("laske_indeksi"));
		final TextBox pituus = TextBox.wrap(DOM.getElementById("pituusInp"));
		final TextBox paino = TextBox.wrap(DOM.getElementById("painoInp"));
		final TextBox ika = TextBox.wrap(DOM.getElementById("ikaInp"));

		pituus.setText("m");
		paino.setText("kg");
		ika.setText("vuotta");

		makeVanishable(pituus);
		makeVanishable(paino);
		makeVanishable(ika);

		final DialogBox dialogBox = new DialogBox();
		final Button closeButton = new Button("Close");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();

		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);

		closeButton.getElement().setId("closeButton");

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML(
				"<b>Lasketaan seuraavien tietojen pohjalta:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		final InlineLabel kIka = InlineLabel.wrap(DOM.getElementById("k-ika"));
		final InlineLabel kPituus = InlineLabel.wrap(DOM
				.getElementById("k-pituus"));
		final InlineLabel kIndeksi = InlineLabel.wrap(DOM
				.getElementById("k-idx"));
		final InlineLabel kPaino = InlineLabel.wrap(DOM
				.getElementById("k-paino"));

		final TextBox alkuIka = TextBox.wrap(getElement("a-ika"));
		final TextBox loppuIka = TextBox.wrap(getElement("l-ika"));
		final Button ikaButton = Button.wrap(getElement("b-ika"));

		calc.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String sPituus = pituus.getText();
				String sPaino = paino.getText();
				String sIka = ika.getText();

				double pituus = 0, paino = 0;
				int ika = 0;
				String prmError = "Virheellinen paino ja/tai pituus ja/tai ikä";
				
				try {
					pituus = Double.parseDouble(sPituus.replace(",", "."));
					paino = Double.parseDouble(sPaino.replace(",", "."));
					ika = Integer.parseInt(sIka);

					String lbltxt = "Lähetetään palvelimelle seuraavat arvot: Pituus "
							+ sPituus
							+ " metriä, paino "
							+ sPaino
							+ " kiloa, ikä " + sIka + " vuotta.";
					textToServerLabel.setText(lbltxt);
				} catch (NumberFormatException e) {
					GWT.log("virhe parsiessa pituutta, painoa, tai ikää");
					textToServerLabel
							.setText(prmError);
					dialogBox.center();
				}

				if (pituus > 0 & paino > 0 & ika > 0) {
					// Joko
					// String indeksi = Laskuri.laskePainoindeksi(pituus, paino)
					// + "";
					// indeksiSvc.kerroIndeksi(indeksi,
					// new AsyncCallback<String>() {
					// Tai
					indeksiSvc.kerroIndeksi(paino, pituus, ika,
						new AsyncCallback<String>() {
						// END_OR
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel
										.setHTML("Virhe laskiessa indeksiä, yritä myöhemmin uudestaan.");
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
				} else {
					GWT.log("Negatiivisia arvoja");
					textToServerLabel
							.setText(prmError);
					dialogBox.center();
				}
			}
		});

		closeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		indeksiSvc.getKeskiset(new AsyncCallback<String[]>() {

			@Override
			public void onSuccess(String[] result) {
				kIka.setText(result[0]);
				kPituus.setText(result[1]);
				kIndeksi.setText(result[2]);
				kPaino.setText(result[3]);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("[getKeskiset] Virhe tietojen haussa.");
			}
		});

		ikaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String sAlku = alkuIka.getText();
				String sLoppu = loppuIka.getText();

				int a_ika = 0, l_ika = 0;
				String intError = "Jompi kumpi ikä on virheellinen, tarkista että ovat kokonaislukuja!";
				
				try {
					a_ika = Integer.parseInt(sAlku);
					l_ika = Integer.parseInt(sLoppu);

					String lbltxt = "Lähetetään palvelimelle seuraavat arvot: Alkuikä "
							+ sAlku + " vuotta, loppuikä "
							+ sLoppu + " vuotta.";
					textToServerLabel.setText(lbltxt);
				} catch (NumberFormatException e) {
					GWT.log("(ikaButton) virhe parsiessa jompaa kumpaa ikää");
					textToServerLabel
							.setText(intError);
					dialogBox.center();
				}

				if (a_ika > 0 & l_ika > 0) {
					
					indeksiSvc.getRajaKeskiset(a_ika, l_ika, new AsyncCallback<String[]>() {
						
						@Override
						public void onSuccess(String[] result) {
							dialogBox.setText("Remote Procedure Call");
							serverResponseLabel
									.removeStyleName("serverResponseLabelError");
							
							String text = "Haetulla ikävälillä käyttäjien keskipituus on <b>" 
									+ result[0] + "</b> metriä, keskipaino on <b>"
									+ result[1] + "</b> kiloa, ja keskimääräinen painoindeksi on <b>"
									+ result[2] + "</b>.";
							
							serverResponseLabel.setHTML(text);
							dialogBox.center();
							closeButton.setFocus(true);
						}
						
						@Override
						public void onFailure(Throwable caught) {
							dialogBox.setText("Remote Procedure Call - Failure");
							serverResponseLabel
									.addStyleName("serverResponseLabelError");
							serverResponseLabel
									.setHTML("Virhe laskiessa keskiarvoja, yritä myöhemmin uudestaan.");
							dialogBox.center();
							closeButton.setFocus(true);
							GWT.log("[getRajaKeskiset] Virhe tietojen haussa.");	
						}
					});
					
				} else {
					GWT.log("Negatiivisia arvoja");
					textToServerLabel
							.setText(intError);
					dialogBox.center();
				}
			}
		});
	}
}
