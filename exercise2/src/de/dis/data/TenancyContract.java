package de.dis.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TenancyContract extends Contract {

    private Date startDate;
    private int duration;
    private double additionalCosts;
    private int estate_id;
    private int person_id;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public int getEstate_id() {
        return estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public void deleteTenancyContract() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String deleteSQL = "DELETE FROM contracts WHERE contract_no = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteSQL);
            pstmt.setString(1, getContractNo());
            pstmt.executeUpdate();
            pstmt.close();

            String deleteSQLTenancyContract = "DELETE FROM tenancy_contracts WHERE contract_no = ?";
            PreparedStatement pstmtTenancyContract = con.prepareStatement(deleteSQLTenancyContract);
            pstmtTenancyContract.setString(1, getContractNo());

            // Führe Anfrage aus
            pstmtTenancyContract.executeUpdate();
            pstmtTenancyContract.close();
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
    public static TenancyContract load(String contractNo) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM tenancy_contracts JOIN contracts ON tenancy_contracts.contract_no = contracts.contract_no WHERE tenancy_contracts.contract_no = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setString(1, contractNo);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TenancyContract tenancyContract = new TenancyContract();
                tenancyContract.setContractNo(contractNo);
                tenancyContract.setDate(rs.getDate("date"));
                tenancyContract.setPlace(rs.getString("place"));
                tenancyContract.setStartDate(rs.getDate("start_date"));
                tenancyContract.setDuration(rs.getInt("duration"));
                tenancyContract.setAdditionalCosts(rs.getDouble("additional_costs"));
                tenancyContract.setEstate_id(rs.getInt("estate_id"));
                tenancyContract.setPerson_id(rs.getInt("person_id"));

                rs.close();
                pstmt.close();
                return tenancyContract;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TenancyContract> loadAll(String contractNo) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM tenancy_contracts JOIN contracts ON tenancy_contracts.contract_no = contracts.estates_id";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();

            ArrayList<TenancyContract> tenancyContracts = new ArrayList<TenancyContract>();
            while(rs.next()) {

                TenancyContract tenancyContract = new TenancyContract();
                tenancyContract.setContractNo(contractNo);
                tenancyContract.setDate(rs.getDate("date"));
                tenancyContract.setPlace(rs.getString("place"));
                tenancyContract.setStartDate(rs.getDate("start_date"));
                tenancyContract.setDuration(rs.getInt("duration"));
                tenancyContract.setAdditionalCosts(rs.getDouble("additional_costs"));
                tenancyContract.setEstate_id(rs.getInt("estate_id"));
                tenancyContract.setPerson_id(rs.getInt("person_id"));
                tenancyContracts.add(tenancyContract);

            }
            rs.close();
            pstmt.close();
            return tenancyContracts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "TenancyContract{" +
                "startDate=" + startDate +
                ", duration=" + duration +
                ", additionalCosts=" + additionalCosts +
                ", estate_id=" + estate_id +
                ", person_id=" + person_id +
                '}';
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
            if (Objects.equals(getContractNo(), "-1")) {
                // Achtung, hier wird noch ein Parameter mitgegeben,
                // damit spC$ter generierte IDs zurC<ckgeliefert werden!
                UUID uuid = UUID.randomUUID();
                setContractNo(uuid.toString());
                String insertSQL = "INSERT INTO contracts(contract_no, date, place) VALUES (?, ?, ?)";

                PreparedStatement pstmt = con.prepareStatement(insertSQL,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmt.setString(1, getContractNo());
                pstmt.setDate(2, getDate());
                pstmt.setString(3, getPlace());

                pstmt.executeUpdate();
                // Hole die Id des engefC<gten Datensatzes
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setContractNo(rs.getString(1));
                }
                rs.close();
                pstmt.close();

                String insertSQLHouse = "INSERT INTO tenancy_contracts(contract_no, start_date, duration, additional_costs, person_id, estate_id) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement pstmtTenancyContract = con.prepareStatement(insertSQLHouse,
                        Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrageparameter und fC<hre Anfrage aus
                pstmtTenancyContract.setString(1, getContractNo());
                pstmtTenancyContract.setDate(2, getStartDate());
                pstmtTenancyContract.setInt(3, getDuration());
                pstmtTenancyContract.setDouble(4, getAdditionalCosts());
                pstmtTenancyContract.setInt(5, getPerson_id());
                pstmtTenancyContract.setInt(6, getEstate_id());

                pstmtTenancyContract.executeUpdate();
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

                String updateSQLHouse = "UPDATE tenancy_contracts SET start_date = ?, duration = ?, additional_costs = ?, person_id = ?, estate_id = ? WHERE contract_no = ?";
                PreparedStatement pstmtHouse = con.prepareStatement(updateSQLHouse);

                // Setze Anfrage Parameter
                pstmtHouse.setDate(1, getStartDate());
                pstmtHouse.setInt(2, getDuration());
                pstmtHouse.setDouble(3, getAdditionalCosts());
                pstmtHouse.setInt(4, getPerson_id());
                pstmtHouse.setInt(5, getEstate_id());
                pstmtHouse.setString(4, getContractNo());
                pstmtHouse.executeUpdate();

                pstmtHouse.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
