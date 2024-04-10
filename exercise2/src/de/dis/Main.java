package de.dis;

import java.sql.ResultSet;
import java.util.Objects;

import de.dis.data.Makler;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_MAKLER = 0;
		final int QUIT = 1;
		final int MENU_ESTATES = 2;
		final int MENU_CONTRACTS = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Immobilien-Verwaltung", MENU_ESTATES);
		mainMenu.addEntry("Vertrags-Verwaltung", MENU_CONTRACTS);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
			case MENU_MAKLER:
				showMaklerMenu();
				break;
			case QUIT:
				return;
			case MENU_ESTATES:
				showEstateMenu();
				break;
			case MENU_CONTRACTS:
				showContractMenu();
				break;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int BACK = 1;
		final int DELETE_MAKLER = 2;
		final int CHANGE_MAKLER = 3;
		final String PASSWORD = "dis24";
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		if (!Objects.equals(FormUtil.readString("Password"), PASSWORD)) {
			System.out.println("wrong Password");
			System.exit(0);
		}
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Delete Makler", DELETE_MAKLER);
		maklerMenu.addEntry("Change Makler", CHANGE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);


		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case BACK:
					return;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case CHANGE_MAKLER:
					changeMakler();
					break;
			}
		}
	}

	/**
	 * Zeigt das Immobilienverwaltungsmenü
	 */
	public static void showEstateMenu() {
		Menu estateMenu = new Menu("Estate-Verwaltung");
	}

	/**
	 * Zeigt das Vertragsverwaltungsmenü
	 */
	public static void showContractMenu() {
		Menu contracteMenu = new Menu("Contract-Verwaltung");
	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}

	public static void deleteMakler() {
		int id = FormUtil.readInt("Makler ID");
		Makler m = Makler.load(id);
		m.delete();
		System.out.println("Makler wurde gelöscht.");
	}

	public static void changeMakler() {
		int id = FormUtil.readInt("Makler ID");
		Makler m = Makler.load(id);
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		System.out.println("Makler wurde geändert.");
	}
}
