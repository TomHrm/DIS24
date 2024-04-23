package de.dis;

import java.util.Objects;

import de.dis.data.Estate;
import de.dis.data.EstateAgent;

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
		final int NEW_ESTATE = 0;
		final int DELETE_ESTATE = 1;
		final int CHANGE_ESTATE = 2;
		final int BACK = 3;

		Menu estateMenu = new Menu("Estate-Verwaltung");
		Estate e = new Estate(FormUtil.readString("EsateId"));
		// check if username exists
		EstateAgent m = null;
		try {
			m = EstateAgent.load(Integer.parseInt("MarklerID"));
			if(m.getId() != estateid) {
				System.out.println("Esate Manager does not match the Makler");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Username does not exist" + e);
			System.exit(0);
		}
		String Password = FormUtil.readString("Password");
		try {
			if (m.getPassword() == Password) {
				System.out.println("Wrong Password");
				System.exit(0);
			}
		} catch (Exception e) {
				System.out.println("Password is incorrect" + e);
				System.exit(0);
		}
		estateMenu.addEntry("Neue Immobilie", 0);
		estateMenu.addEntry("Immobilie löschen", 1);
		estateMenu.addEntry("Immobilie ändern", 2);
		estateMenu.addEntry("Zurück zum Hauptmenü", 3);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();

			switch(response) {
			case NEW_ESTATE:
				createEstate();
				break;
			case DELETE_ESTATE:
				deleteEstate();
				break;
			case CHANGE_ESTATE:
				changeEstate();
				break;
			}
		}
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
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}

	public static void deleteMakler() {
		int id = FormUtil.readInt("Makler ID");
		EstateAgent m = EstateAgent.load(id);
		m.delete();
		System.out.println("Makler wurde gelöscht.");
	}

	public static void changeMakler() {
		int id = FormUtil.readInt("Makler ID");
		EstateAgent m = EstateAgent.load(id);
		m.setName(FormUtil.readString("Name:" + m.getName()));
		m.setAddress(FormUtil.readString("Adresse:" + m.getAddress()));
		m.setLogin(FormUtil.readString("Login:" + m.getLogin()));
		m.setPassword(FormUtil.readString("Passwort:" + m.getPassword()));
		m.save();
		System.out.println("Makler wurde geändert.");
	}

	public static void createEstate() {
		Estate e = new Estate();
		e.setMaklerId(FormUtil.readInt("Makler ID"));
		e.setCity(FormUtil.readString("Stadt"));
		e.setPostalCode(FormUtil.readString("PLZ"));
		e.setStreet(FormUtil.readString("Straße"));
		e.setStreetNumber(FormUtil.readString("Hausnummer"));
		e.setSquareArea(FormUtil.readInt("Quadratmeter"));
		e.setPrice(Double.parseDouble(FormUtil.readString("Preis")));
		e.setGarden(Boolean.parseBoolean(FormUtil.readString("Garten")));
		e.setBalcony(Boolean.parseBoolean(FormUtil.readString("Balkon")));
		e.setBuiltInKitchen(Boolean.parseBoolean(FormUtil.readString("Einbauküche")));
		e.save();
		System.out.println("Immobilie mit der ID "+e.getId()+" wurde erzeugt.");
	}

	public static void deleteEstate() {
		int id = FormUtil.readInt("Immobilien ID");
		Estate e = Estate.load(id);
		e.delete();
		System.out.println("Immobilie wurde gelöscht.");
	}

	public static void changeEstate() {
		int id = FormUtil.readInt("Immobilien ID");
		Estate e = Estate.load(id);
		e.setMaklerId(FormUtil.readInt("Makler ID:" + e.getMaklerId()));
		e.setCity(FormUtil.readString("Stadt:" + e.getCity()));
		e.setPostalCode(FormUtil.readString("PLZ:" + e.getPostalCode()));
		e.setStreet(FormUtil.readString("Straße:" + e.getStreet()));
		e.setStreetNumber(FormUtil.readString("Hausnummer:" + e.getStreetNumber()));
		e.setSquareArea(FormUtil.readInt("Quadratmeter:" + e.getSquareArea()));
		e.setPrice(Double.parseDouble(FormUtil.readString("Preis" + e.getPrice())));
		e.setGarden(Boolean.parseBoolean(FormUtil.readString("Garten" + e.isGarden())));
		e.setBalcony(Boolean.parseBoolean(FormUtil.readString("Balkon" + e.isBalcony())));
		e.setBuiltInKitchen(Boolean.parseBoolean(FormUtil.readString("Einbauküche" + e.isBuiltInKitchen())));
		e.save();
		System.out.println("Immobilie wurde geändert.");
	}
}
