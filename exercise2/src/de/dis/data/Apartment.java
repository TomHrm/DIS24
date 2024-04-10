package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Makler-Bean
 *
 * Beispiel-Tabelle:
 * CREATE TABLE makler (
 * name varchar(255),
 * address varchar(255),
 * login varchar(40) UNIQUE,
 * password varchar(40),
 * id serial primary key);
 */
public class Apartment extends Estate {
	private int floor;
	private double rent;
	private int rooms;
	private boolean balcony;
	private boolean built_in_kitchen;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double getRent() {return rent;}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public int setRooms() {
		return rooms;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}

	public boolean getBalcony() {return balcony;}

	public void setBuiltInKitchen(boolean built_in_kitchen) {this.built_in_kitchen = built_in_kitchen;}

	public boolean getBuiltInKitchen() {return built_in_kitchen;}

	public void delete() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			String deleteSQL = "DELETE FROM estate_agents WHERE agent_id = ?";
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, getId());

			// Führe Anfrage aus
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
