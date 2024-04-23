package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class House extends Estate {

	private int floors;
	private double price;
	private boolean garden;

	public int getFloors(){
		return floors;
	}

	public void setFloors(int floors){
		this.floors = floors;
	}

	public double getPrice(){
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean getGarden(){
		return garden;
	}
	public void setGarden(boolean garden){
		this.garden = garden;
	}

	public void deleteHouse() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String deleteSQL = "DELETE FROM estates WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, getId());
			pstmt.executeUpdate();
			pstmt.close();

			String deleteSQLHouse = "DELETE FROM houses WHERE estate_id = ?";
			PreparedStatement pstmtHouse = con.prepareStatement(deleteSQLHouse);
			pstmtHouse.setInt(1, getId());

			// Führe Anfrage aus
			pstmtHouse.executeUpdate();
			pstmtHouse.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	// TODO:
	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param houseId ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static House load(int houseId) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM houses JOIN estates ON houses.estate_id = estates.estates_id WHERE estate_id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, houseId);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				House house = new House();
				house.setId(houseId);
				house.setAgent_id(rs.getInt("agent_id"));
				house.setPostalCode(rs.getString("postal_code"));
				house.setCity(rs.getString("city"));
				house.setStreet(rs.getString("street"));
				house.setStreetNumber(rs.getInt("street_number"));
				house.setFloors(rs.getInt("floors"));
				house.setPrice(rs.getDouble("price"));
				house.setGarden(rs.getBoolean("garden"));

				rs.close();
				pstmt.close();
				return house;
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
				String insertSQL = "INSERT INTO estates(agent_id, postal_code, city, street, street_number, square_area) VALUES (?, ?, ?, ?, ?,?)";

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
				rs.close();
				pstmt.close();

				String insertSQLHouse = "INSERT INTO houses(estate_id, floors, price, garden) VALUES (?, ?, ?, ?)";

				PreparedStatement pstmtHouse = con.prepareStatement(insertSQLHouse,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmtHouse.setInt(1, getId());
				pstmtHouse.setInt(2, getFloors());
				pstmtHouse.setDouble(3, getPrice());
				pstmtHouse.setBoolean(4, getGarden());

				pstmtHouse.executeUpdate();
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

				String updateSQLHouse = "UPDATE houses SET floors = ?, price = ?, garden = ? WHERE estate_id = ?";
				PreparedStatement pstmtHouse = con.prepareStatement(updateSQLHouse);

				// Setze Anfrage Parameter
				pstmtHouse.setInt(1, getFloors());
				pstmtHouse.setDouble(2, getPrice());
				pstmtHouse.setBoolean(3, getGarden());
				pstmtHouse.setInt(4, getId());
				pstmtHouse.executeUpdate();

				pstmtHouse.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
