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

    public boolean addBloodUnit(String bloodGroup, String collectionDate,
                               String expiryDate, int donorId) {

        String sql = "INSERT INTO BloodUnit(BloodGroup, CollectionDate, ExpiryDate, Status, DonorID) VALUES(?, ?, ?, 'Available', ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bloodGroup);
            ps.setString(2, collectionDate);
            ps.setString(3, expiryDate);
            ps.setInt(4, donorId);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Add unit failed: " + e.getMessage());
        } finally {
            System.out.println("Add Blood Unit attempted");
        }
        return false;
    }

    @Override
    public boolean add(Object... obj) {
        return addBloodUnit(
            (String) obj[0],
            (String) obj[1],
            (String) obj[2],
            (int) obj[3]
        );
    }

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
            System.out.println("Inventory fetch failed: " + e.getMessage());
        }
        return list;
    }
}