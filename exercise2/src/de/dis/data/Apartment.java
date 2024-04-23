package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment extends Estate {

	private int floor;
	private double rent;
	private int rooms;
	private boolean balcony;
	private boolean builtInKitchen;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double getRent() {
		return rent;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public boolean isBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean isBuiltInKitchen() {
		return builtInKitchen;
	}

	public void setBuiltInKitchen(boolean builtInKitchen) {
		this.builtInKitchen = builtInKitchen;
	}

	public void deleteApartment() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String deleteSQL = "DELETE FROM estates WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, getId());
			pstmt.executeUpdate();
			pstmt.close();

			String deleteSQLApartment = "DELETE FROM appartments WHERE estate_id = ?";
			PreparedStatement pstmtApartment = con.prepareStatement(deleteSQLApartment);
			pstmtApartment.setInt(1, getId());

			// Führe Anfrage aus
			pstmtApartment.executeUpdate();
			pstmtApartment.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	// TODO:
	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param apartmentId ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static Apartment load(int apartmentId) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM apartments JOIN estates ON apartments.estate_id = estates.estates_id WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, apartmentId);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Apartment apartment = new Apartment();
				apartment.setId(apartmentId);
				apartment.setAgent_id(rs.getInt("agent_id"));
				apartment.setPostalCode(rs.getString("postal_code"));
				apartment.setCity(rs.getString("city"));
				apartment.setStreet(rs.getString("street"));
				apartment.setStreetNumber(rs.getInt("street_number"));
				apartment.setFloor(rs.getInt("floor"));
				apartment.setRent(rs.getDouble("rent"));
				apartment.setRooms(rs.getInt("rooms"));
				apartment.setBalcony(rs.getBoolean("balcony"));
				apartment.setBuiltInKitchen(rs.getBoolean("built_in_kitchen"));

				rs.close();
				pstmt.close();
				return apartment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	// TODO:
	/**
	 * Speichert den Makler in der Datenbank. Ist noch keine ID vergeben
	 * worden, wird die generierte Id von der DB geholt und dem Model übergeben.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO estates(agent_id, postal_code, city, street, street_number, square_area) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getAgent_id());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getCity());
				pstmt.setString(4, getStreet());
				pstmt.setInt(5, getStreetNumber());
				pstmt.setInt(6, getSquareArea());
				pstmt.executeUpdate();
				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}
				System.out.println("HERE");
				rs.close();
				pstmt.close();

				String insertSQLApartment = "INSERT INTO apartments(estate_id, floor, rent, rooms, balcony, built_in_kitchen) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmtApartment = con.prepareStatement(insertSQLApartment,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmtApartment.setInt(1, getId());
				pstmtApartment.setInt(2, getFloor());
				pstmtApartment.setDouble(3, getRent());
				pstmtApartment.setInt(4, getRooms());
				pstmtApartment.setBoolean(5, isBalcony());
				pstmtApartment.setBoolean(6, isBuiltInKitchen());

				pstmtApartment.executeUpdate();
				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estates SET agent_id = ?, postal_code = ?, city = ?, street = ?, street_number = ?, square_area = ? WHERE estate_id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setInt(1, getAgent_id());
				pstmt.setString(2, getPostalCode());
				pstmt.setString(3, getCity());
				pstmt.setString(4, getStreet());
				pstmt.setInt(5, getStreetNumber());
				pstmt.setInt(6, getSquareArea());
				pstmt.setInt(7, getId());
				pstmt.executeUpdate();

				pstmt.close();

				String updateSQLApartment = "UPDATE apartments SET estate_id = ?, floor = ?, rent = ?, rooms = ?, balcony = ?, built_in_kitchen = ? WHERE estate_id = ?";
				PreparedStatement pstmtApartment = con.prepareStatement(updateSQLApartment);

				// Setze Anfrage Parameter
				pstmtApartment.setInt(1, getId());
				pstmtApartment.setInt(2, getFloor());
				pstmtApartment.setDouble(3, getRent());
				pstmtApartment.setInt(4, getRooms());
				pstmtApartment.setBoolean(5, isBalcony());
				pstmtApartment.setBoolean(6, isBuiltInKitchen());
				pstmtApartment.setInt(7, getId());
				pstmtApartment.executeUpdate();

				pstmtApartment.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
