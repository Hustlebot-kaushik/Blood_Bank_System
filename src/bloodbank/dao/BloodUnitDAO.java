package bloodbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bloodbank.util.DBConnection;

public class BloodUnitDAO implements DAO {

    // ── ADD blood unit ─────────────────────────────────────────────────────
    public boolean addBloodUnit(String bloodGroup, String collectionDate,
                                String expiryDate, int donorId) {

        String sql = "INSERT INTO BloodUnit(BloodGroup, CollectionDate, ExpiryDate, Status, DonorID) "
                   + "VALUES(?, ?, ?, 'Available', ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bloodGroup);
            ps.setString(2, collectionDate);
            ps.setString(3, expiryDate);
            ps.setInt(4, donorId);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding blood unit: " + e.getMessage());
        } finally {
            System.out.println("addBloodUnit() attempted.");
        }
        return false;
    }

    // ── Interface method (Polymorphism: Method Overriding) ────────────────
    @Override
    public boolean add(Object... obj) {
        return addBloodUnit(
            (String) obj[0],
            (String) obj[1],
            (String) obj[2],
            (int)    obj[3]
        );
    }

    // ── UPDATE blood unit status ───────────────────────────────────────────
    public boolean updateBloodUnitStatus(int unitId, String newStatus) {
        String sql = "UPDATE BloodUnit SET Status = ? WHERE UnitID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, unitId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating blood unit status: " + e.getMessage());
        } finally {
            System.out.println("updateBloodUnitStatus() attempted for UnitID: " + unitId);
        }
        return false;
    }

    // ── SELECT inventory summary ───────────────────────────────────────────
    public List<String[]> getInventorySummary() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT BloodGroup, TotalUnits, AvailableUnits FROM Inventory";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("BloodGroup"),
                    rs.getString("TotalUnits"),
                    rs.getString("AvailableUnits")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        }
        return list;
    }
}