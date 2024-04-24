package de.dis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import de.dis.data.*;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) throws FileNotFoundException {

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
					showMainMenu();
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
		final int NEW_APARTMENT = 0;
		final int NEW_HOUSE = 1;
		final int DELETE_ESTATE = 2;
		final int CHANGE_APARTMENT = 3;
		final int CHANGE_HOUSE = 4;
		final int BACK = 5;

		Menu estateMenu = new Menu("Estate-Verwaltung");
//		Estate e = new Estate(FormUtil.readString("EstateId"));
//		// check if username exists
//		EstateAgent m = null;
//		try {
//			m = EstateAgent.load(Integer.parseInt("MarklerID"));
//			if(m.getId() != estateid) {
//				System.out.println("Esate Manager does not match the Makler");
//				System.exit(0);
//			}
//		} catch (Exception e) {
//			System.out.println("Username does not exist" + e);
//			System.exit(0);
//		}
//		String Password = FormUtil.readString("Password");
//		try {
//			if (m.getPassword() == Password) {
//				System.out.println("Wrong Password");
//				System.exit(0);
//			}
//		} catch (Exception e) {
//				System.out.println("Password is incorrect" + e);
//				System.exit(0);
//		}
		estateMenu.addEntry("Neues Apartment", 0);
		estateMenu.addEntry("Neues Haus", 1);
		estateMenu.addEntry("Immobilie löschen", 2);
		estateMenu.addEntry("Apartment ändern", 3);
		estateMenu.addEntry("Haus ändern", 4);
		estateMenu.addEntry("Zurück zum Hauptmenü", 5);

		//Verarbeite Eingabe
		while(true) {
			int response = estateMenu.show();

			switch(response) {
			case NEW_APARTMENT:
				createApartment();
				break;
			case NEW_HOUSE:
				createHouse();
				break;
			case DELETE_ESTATE:
				deleteEstate();
				break;
			case CHANGE_APARTMENT:
				changeApartment();
				break;
			case CHANGE_HOUSE:
				changeHouse();
				break;
			case BACK:
				showMainMenu();
				return;
			}
		}
	}

	/**
	 * Zeigt das Vertragsverwaltungsmenü
	 */
	public static void showContractMenu() {
		final int CREATE_PERSON = 1;
		final int NEW_SALE_CONTRACT = 2;
		final int NEW_RENT_CONTRACT = 3;
		final int SEE_ALL_CONTRACTS = 4;
		final int BACK = 5;
		Menu contracteMenu = new Menu("Contract-Verwaltung");
		contracteMenu.addEntry("Neue Person erstellen", 1);
		contracteMenu.addEntry("Neuer Verkaufs Vertrag", 2);
		contracteMenu.addEntry("Neuer Mietvertrag", 3);
		contracteMenu.addEntry("Alle Verträge anzeigen anzeigen", 4);
		contracteMenu.addEntry("Zurück zum Hauptmenü", 5);

		while(true) {
			int response = contracteMenu.show();

			switch(response) {
				case CREATE_PERSON:
					createPerson();
					break;
				case NEW_SALE_CONTRACT:
					createSaleContract();
					break;
				case NEW_RENT_CONTRACT:
					createRentContract();
					break;
				case SEE_ALL_CONTRACTS:
					showAllContracts();
					break;
				case BACK:
					showMainMenu();
					return;
			}
		}
	}

	private static void showAllContracts() {
		ArrayList<TenancyContract> tenancyContracts = loadAllTenancyContracts();
		ArrayList<PurchaseContract> purchaseContracts = loadAllPurchaseContracts();

		System.out.println("Mietverträge:");
		for(TenancyContract tenancyContract : tenancyContracts) {
			System.out.println(tenancyContract);
		}

		System.out.println("Kaufverträge:");
		for(PurchaseContract purchaseContract : purchaseContracts) {
			System.out.println(purchaseContract);
		}
	}

	public static ArrayList<TenancyContract> loadAllTenancyContracts() {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT contract_no FROM tenancy_contracts";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();

			ArrayList<TenancyContract> tenancyContracts = new ArrayList<TenancyContract>();
			while(rs.next()) {

				TenancyContract tenancyContract= new TenancyContract();
				tenancyContracts.add(tenancyContract.load(rs.getString("contract_no")));
			}
			rs.close();
			pstmt.close();
			return tenancyContracts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static ArrayList<PurchaseContract> loadAllPurchaseContracts() {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT contract_no FROM purchase_contracts";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();

			ArrayList<PurchaseContract> purchaseContracts = new ArrayList<PurchaseContract>();
			while(rs.next()) {

				PurchaseContract purchaseContract = new PurchaseContract();
				purchaseContracts.add(purchaseContract.load(rs.getString("contract_no")));
			}
			rs.close();
			pstmt.close();
			return purchaseContracts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void createRentContract() {
		TenancyContract tenancyContract = new TenancyContract();
		tenancyContract.setContractNo(FormUtil.readString("CONTRACT_NO -1 for new"));
		tenancyContract.setDate(FormUtil.readDate("Date"));
		tenancyContract.setPlace(FormUtil.readString("Place"));
		tenancyContract.setStartDate(FormUtil.readDate("Start Date"));
		tenancyContract.setDuration(FormUtil.readInt("Duration"));
		tenancyContract.setAdditionalCosts(Double.parseDouble(FormUtil.readString("Zusätzliche Kosten")));
		tenancyContract.setPerson_id(FormUtil.readInt("Person ID"));
		tenancyContract.setEstate_id(FormUtil.readInt("Estate-ID"));
		tenancyContract.save();

		System.out.println("Miet Vertrag mit der Vertragsnummer: " + tenancyContract.getContractNo()+" wurde erzeugt.");
	}

	private static void createSaleContract() {
		PurchaseContract purchaseContract = new PurchaseContract();
		purchaseContract.setContractNo(FormUtil.readString("CONTRACT_NO -1 for new"));
		purchaseContract.setDate(FormUtil.readDate("Date"));
		purchaseContract.setPlace(FormUtil.readString("Place"));
		purchaseContract.setNoOfInstallments(FormUtil.readInt("Anzahl der Zahlungen"));
		purchaseContract.setInterestRate(Double.parseDouble(FormUtil.readString("Interest Rate")));
		purchaseContract.setPerson_id(FormUtil.readInt("Person ID"));
		purchaseContract.setEstate_id(FormUtil.readInt("Estate-ID"));
		purchaseContract.save();
		System.out.println("Verkaufs Vertrag mit der Vertragsnummer: " + purchaseContract.getContractNo()+" wurde erzeugt.");
	}

	/**
	 * Check if Markler exists
	 */
	public static void checkMakler(String id) {
		Estate e = new Estate();
		// check if username exists
		EstateAgent m = null;
		try {
			m = EstateAgent.load(Integer.parseInt(id));
			if(m.getId() != e.getAgent_id()) {
				System.out.println("Esate Manager does not match the Makler");
				System.exit(0);
			}
		} catch (Exception ex) {
			System.out.println("Username does not exist" + ex);
			System.exit(0);
		}
		String Password = FormUtil.readString("Password");
		try {
			if (m.getPassword() == Password) {
				System.out.println("Wrong Password");
				System.exit(0);
			}
		} catch (Exception ex) {
			System.out.println("Password is incorrect" + ex);
			System.exit(0);
		}
	}

	// list all apartments
	public static void listApartments() {
		try {
			System.out.println("Apartments:");
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartments";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();

			// Iteriere über die Ergebnisse und gebe sie aus
			while(rs.next()) {
				System.out.println("ID: "+rs.getInt("estate_id")+" Agent ID: "+rs.getInt("agent_id")+" City: "+rs.getString("city")+" Postal Code: "+rs.getString("postal_code")+" Street: "+rs.getString("street")+" Street Number: "+rs.getInt("street_number")+" Square Area: "+rs.getInt("square_area")+" Rent: "+rs.getDouble("rent")+" Floor: "+rs.getInt("floor")+" Rooms: "+rs.getInt("rooms")+" Balcony: "+rs.getBoolean("balcony")+" Built-In Kitchen: "+rs.getBoolean("built_in_kitchen"));
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * List all Markler
	 */
	public static void listMakler() {
		try {
			System.out.println("Makler:");
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agents";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();

			// Iteriere über die Ergebnisse und gebe sie aus
			while(rs.next()) {
				System.out.println("ID: "+rs.getInt("agent_id")+" Name: "+rs.getString("name")+" Address: "+rs.getString("address"));
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// list all Haus
	public static void listHouses() {
		try {
			System.out.println("Häuser:");
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM houses";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();

			// Iteriere über die Ergebnisse und gebe sie aus
			while(rs.next()) {
				System.out.println("ID: "+rs.getInt("estate_id")+" Agent ID: "+rs.getInt("agent_id")+" City: "+rs.getString("city")+" Postal Code: "+rs.getString("postal_code")+" Street: "+rs.getString("street")+" Street Number: "+rs.getInt("street_number")+" Square Area: "+rs.getInt("square_area")+" Price: "+rs.getDouble("price")+" Garden: "+rs.getBoolean("garden"));
			}

			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void createPerson() {
		Person p = new Person();
		p.setPersonId(-1);
		p.setFirstName(FormUtil.readString("First Name"));
		p.setName(FormUtil.readString("Name"));
		p.setAddress(FormUtil.readString("Address"));
		p.save();

		System.out.println("Person mit der ID: " + p.getPersonId()+" wurde erzeugt.");
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
		listMakler();
		int id = FormUtil.readInt("Makler ID");
		EstateAgent m = EstateAgent.load(id);
		m.delete();
		System.out.println("Makler wurde gelöscht.");
	}

	public static void changeMakler() {
		listMakler();
		int id = FormUtil.readInt("Makler ID");
		EstateAgent m = EstateAgent.load(id);
		m.setName(FormUtil.readString("Name:" + m.getName()));
		m.setAddress(FormUtil.readString("Adresse:" + m.getAddress()));
		m.setLogin(FormUtil.readString("Login:" + m.getLogin()));
		m.setPassword(FormUtil.readString("Passwort:" + m.getPassword()));
		m.save();
		System.out.println("Makler wurde geändert.");
	}

	public static void createHouse() {
		House e = new House();
		e.setId(-1);
		int id = FormUtil.readInt("Makler ID");
		e.setAgent_id(id);
		e.setAgent_id(id);
		e.setCity(FormUtil.readString("Stadt"));
		e.setPostalCode(FormUtil.readString("PLZ"));
		e.setStreet(FormUtil.readString("Straße (Ohne Hausnummer)"));
		e.setStreetNumber(FormUtil.readInt("Hausnummer"));
		e.setSquareArea(FormUtil.readInt("Quadratmeter"));
		e.setFloors(FormUtil.readInt("Etagen"));
		e.setPrice(Double.parseDouble(FormUtil.readString("Preis")));
		e.setGarden(Boolean.parseBoolean(FormUtil.readString("Garten")));
		e.save();
		System.out.println("Haus mit der ID "+e.getId()+" wurde erzeugt.");
	}

	public static void createApartment() {
		Apartment e = new Apartment();
		e.setId(-1);
		e.setAgent_id(FormUtil.readInt("Makler ID"));
		e.setCity(FormUtil.readString("Stadt"));
		e.setPostalCode(FormUtil.readString("PLZ"));
		e.setStreet(FormUtil.readString("Straße (Ohne Hausnummer)"));
		e.setStreetNumber(FormUtil.readInt("Hausnummer"));
		e.setSquareArea(FormUtil.readInt("Quadratmeter"));
		e.setRent(FormUtil.readInt("Miete"));
		e.setFloor(FormUtil.readInt("Ebene"));
		e.setRooms(FormUtil.readInt("Zimmer"));
		e.setBalcony(Boolean.parseBoolean(FormUtil.readString("Balkon")));
		e.setBuiltInKitchen(Boolean.parseBoolean(FormUtil.readString("Einbauküche")));
		e.save();
		System.out.println("Apartment mit der ID "+e.getId()+" wurde erzeugt.");
	}

	public static void deleteEstate() {
		listHouses();
		int id = FormUtil.readInt("Immobilien ID");
		Estate e = Estate.load(id);
		e.delete();
		System.out.println("Immobilie wurde gelöscht.");
	}

	public static void changeHouse() {
		listHouses();
		int id = FormUtil.readInt("Immobilien ID");
		House e = House.load(id);
		e.setId(-1);
		e.setAgent_id(FormUtil.readInt("Makler ID:" + e.getAgent_id()));
		e.setCity(FormUtil.readString("Stadt:" + e.getCity()));
		e.setPostalCode(FormUtil.readString("PLZ:" + e.getPostalCode()));
		e.setStreet(FormUtil.readString("Straße:" + e.getStreet()));
		e.setStreetNumber(FormUtil.readInt("Hausnummer:" + e.getStreetNumber()));
		e.setSquareArea(FormUtil.readInt("Quadratmeter:" + e.getSquareArea()));
		e.setPrice(Double.parseDouble(FormUtil.readString("Preis" + e.getPrice())));
		e.setGarden(Boolean.parseBoolean(FormUtil.readString("Garten" + e.getGarden())));
		e.save();
		System.out.println("Haus wurde geändert.");
	}

	public static void changeApartment() {
		listApartments();
		int id = FormUtil.readInt("Immobilien ID");
		Apartment e = Apartment.load(id);
		e.setAgent_id(FormUtil.readInt("Makler ID:" + e.getAgent_id()));
		e.setCity(FormUtil.readString("Stadt:" + e.getCity()));
		e.setPostalCode(FormUtil.readString("PLZ:" + e.getPostalCode()));
		e.setStreet(FormUtil.readString("Straße:" + e.getStreet()));
		e.setStreetNumber(FormUtil.readInt("Hausnummer:" + e.getStreetNumber()));
		e.setSquareArea(FormUtil.readInt("Quadratmeter:" + e.getSquareArea()));
		e.setBalcony(Boolean.parseBoolean(FormUtil.readString("Balkon" + e.isBalcony())));
		e.setBuiltInKitchen(Boolean.parseBoolean(FormUtil.readString("Einbauküche" + e.isBuiltInKitchen())));
		e.save();
		System.out.println("Apartment wurde geändert.");
	}
}
