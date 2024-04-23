package de.dis.data;

import java.sql.*;
public class Contract {
    private String contractNo;
    private Date date;
    private String place;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

//    public void delete() {
//        // Hole Verbindung
//        Connection con = DbConnectionManager.getInstance().getConnection();
//
//        try {
//            String deleteSQL = "DELETE FROM estate WHERE agent_id = ?";
//            PreparedStatement pstmt = con.prepareStatement(deleteSQL);
//            pstmt.setInt(1, getId());
//
//            // Führe Anfrage aus
//            pstmt.executeUpdate();
//            pstmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    // TODO:
    /**
     * Lädt einen Makler aus der Datenbank
     * @param estateId ID des zu ladenden Maklers
     * @return Makler-Instanz
     */
//    public static Estate load(int estateId) {
//        try {
//            // Hole Verbindung
//            Connection con = DbConnectionManager.getInstance().getConnection();
//
//            // Erzeuge Anfrage
//            String selectSQL = "SELECT * FROM estate_agents WHERE agent_id = ?";
//            PreparedStatement pstmt = con.prepareStatement(selectSQL);
//            pstmt.setInt(1, estateId);
//
//            // Führe Anfrage aus
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                Estate es = new Estate();
//                es.setId(estateId);
//                es.setName(rs.getString("name"));
//                es.setAgent_id(rs.getInt("agent_id"));
//                es.setPostalCode(rs.getString("postal_code"));
//                es.setCity(rs.getString("city"));
//                es.setStreet(rs.getString("street"));
//                es.setStreetNumber(rs.getString("street_number"));
//
//                rs.close();
//                pstmt.close();
//                return es;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    // TODO:
    /**
     * Speichert den Makler in der Datenbank. Ist noch keine ID vergeben
     * worden, wird die generierte Id von der DB geholt und dem Model übergeben.
     */
//	public void save() {
//		// Hole Verbindung
//		Connection con = DbConnectionManager.getInstance().getConnection();
//
//		try {
//			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
//			if (getId() == -1) {
//				// Achtung, hier wird noch ein Parameter mitgegeben,
//				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
//				String insertSQL = "INSERT INTO estates(name, address, agent_id) VALUES (?, ?, ?, ?)";
//
//				PreparedStatement pstmt = con.prepareStatement(insertSQL,
//					Statement.RETURN_GENERATED_KEYS);
//
//				// Setze Anfrageparameter und fC<hre Anfrage aus
//				pstmt.setString(1, getName());
//
//				pstmt.executeUpdate();
//
//				// Hole die Id des engefC<gten Datensatzes
//				ResultSet rs = pstmt.getGeneratedKeys();
//				if (rs.next()) {
//					setId(rs.getInt(1));
//				}
//
//				rs.close();
//				pstmt.close();
//			} else {
//				// Falls schon eine ID vorhanden ist, mache ein Update...
//				String updateSQL = "UPDATE estate_agents SET name = ?, address = ?, login = ?, password = ? WHERE agent_id = ?";
//				PreparedStatement pstmt = con.prepareStatement(updateSQL);
//
//				// Setze Anfrage Parameter
//				pstmt.setString(1, getName());
//				pstmt.setString(2, getAddress());
//				pstmt.setString(3, getLogin());
//				pstmt.setString(4, getPassword());
//				pstmt.setInt(5, getId());
//				pstmt.executeUpdate();
//
//				pstmt.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
