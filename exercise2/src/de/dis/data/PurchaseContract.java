package de.dis.data;

import java.sql.*;
import java.util.Objects;

public class PurchaseContract extends Contract {

    private int noOfInstallments;
    private double interestRate;
    private int person_id;
    private int estate_id;

    public int getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(int noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public int getEstate_id() {
        return estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

    public void deletePurchaseContract() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String deleteSQL = "DELETE FROM contracts WHERE contract_no = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1, getContractNo());
            pstmt.executeUpdate();
            pstmt.close();

            String deleteSQLPurchaseContract = "DELETE FROM purchase_contracts WHERE contract_no = ?";
            PreparedStatement pstmtPurchaseContract = con.prepareStatement(deleteSQLPurchaseContract);
            pstmtPurchaseContract.setString(1, getContractNo());

            // Führe Anfrage aus
            pstmtPurchaseContract.executeUpdate();
            pstmtPurchaseContract.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // TODO:
    /**
     * Lädt einen Makler aus der Datenbank
     * @param contractNo ID des zu ladenden Maklers
     * @return Makler-Instanz
     */
    public static PurchaseContract load(String contractNo) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM purchase_contracts JOIN contracts ON purchase_contracts.contract_no = contracts.contract_no WHERE contract_no = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, contractNo);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PurchaseContract purchaseContract = new PurchaseContract();
                purchaseContract.setContractNo(contractNo);
                purchaseContract.setDate(rs.getDate("date"));
                purchaseContract.setPlace(rs.getString("place"));
                purchaseContract.setNoOfInstallments(rs.getInt("no_of_installments"));
                purchaseContract.setInterestRate(rs.getDouble("interest_rate"));
                purchaseContract.setPerson_id(rs.getInt("person_id"));
                purchaseContract.setEstate_id(rs.getInt("estate_id"));

                rs.close();
                pstmt.close();
                return purchaseContract;
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
            if (Objects.equals(getContractNo(), "")) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                String insertSQL = "INSERT INTO contract(date, place) VALUES (?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setDate(1, getDate());
                pstmt.setString(2, getPlace());

                pstmt.executeUpdate();
                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setContractNo(rs.getString(1));
                }
                rs.close();
                pstmt.close();

                String insertSQLHouse = "INSERT INTO tenancy_contract(contract_no, no_of_installments, interst_rate, person_id, estate_id) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement pstmtPurchaseContract = con.prepareStatement(insertSQLHouse,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmtPurchaseContract.setString(1, getContractNo());
                pstmtPurchaseContract.setInt(2, getNoOfInstallments());
                pstmtPurchaseContract.setDouble(3, getInterestRate());
                pstmtPurchaseContract.setInt(4, getPerson_id());
                pstmtPurchaseContract.setInt(5, getEstate_id());

                pstmtPurchaseContract.executeUpdate();
                rs.close();
                pstmt.close();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "UPDATE contracts SET date = ?, place = ? WHERE contract_no = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setDate(1, getDate());
                pstmt.setString(2, getPlace());
                pstmt.setString(3, getContractNo());
                pstmt.executeUpdate();

                pstmt.close();

                String updateSQLHouse = "UPDATE tenancy_contracts SET no_of_installments = ?, interst_rate = ?, person_id = ?, estate_id = ? WHERE contract_no = ?";
                PreparedStatement pstmtHouse = con.prepareStatement(updateSQLHouse);

                // Setze Anfrage Parameter
                pstmtHouse.setInt(1, getNoOfInstallments());
                pstmtHouse.setDouble(2, getInterestRate());
                pstmtHouse.setInt(3, getPerson_id());
                pstmtHouse.setInt(4, getEstate_id());
                pstmtHouse.setString(5, getContractNo());
                pstmtHouse.executeUpdate();

                pstmtHouse.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
