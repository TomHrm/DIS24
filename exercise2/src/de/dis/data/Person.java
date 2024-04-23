package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {

    private int personId;
    private String firstName;
    private String name;
    private String address;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String  firstName) {
        this.firstName = firstName;
    }

    public String  getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String  address) {
        this.address = address;
    }

    public void deletePerson() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String deleteSQL = "DELETE FROM persons WHERE person_id = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteSQL);
            pstmt.setInt(1, getPersonId());
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // TODO:
    /**
     * Lädt einen Makler aus der Datenbank
     * @param personId ID des zu ladenden Maklers
     * @return Makler-Instanz
     */
    public static Person load(int personId) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM persons WHERE person_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, personId);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Person house = new Person();
                house.setPersonId(personId);
                house.setFirstName(rs.getString("first_name"));
                house.setName(rs.getString("name"));
                house.setAddress(rs.getString("address"));

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
            if (getPersonId() == -1) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO persons(first_name, name, address) VALUES (?, ?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setString(1, getFirstName());
                pstmt.setString(2, getName());
                pstmt.setString(3, getAddress());
                pstmt.executeUpdate();
                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setPersonId(rs.getInt(1));
                }
                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE estates SET first_name = ?, name = ?, address = ? WHERE person_id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getFirstName());
                pstmt.setString(2, getName());
                pstmt.setString(3, getAddress());
                pstmt.setInt(4, getPersonId());
                pstmt.executeUpdate();

                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

