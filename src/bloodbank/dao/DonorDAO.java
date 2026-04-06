package bloodbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bloodbank.exception.InvalidAgeException;
import bloodbank.util.DBConnection;

public class DonorDAO implements DAO {

    // ── ADD (full details) ─────────────────────────────────────────────────
    public boolean addDonor(String name, int age, String bloodGroup,
                             String phone, String email, String address) {

        String sql = "INSERT INTO Donor(Name, Age, BloodGroup, Phone, Email, Address) "
                   + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (age <= 0 || age > 120) {
                throw new InvalidAgeException("Age must be between 1 and 120. Got: " + age);
            }

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, bloodGroup);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, address);

            ps.executeUpdate();
            return true;

        } catch (InvalidAgeException | SQLException e) {
            System.out.println("Error adding donor: " + e.getMessage());
        } finally {
            System.out.println("addDonor() attempted for: " + name);
        }
        return false;
    }

    // ── ADD (overloaded – Polymorphism: Method Overloading) ────────────────
    // Adds a donor with only name and blood group (minimal registration)
    public boolean addDonor(String name, String bloodGroup) {
        String sql = "INSERT INTO Donor(Name, BloodGroup) VALUES(?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, bloodGroup);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding quick donor: " + e.getMessage());
        }
        return false;
    }

    // ── Interface method (Polymorphism: Method Overriding) ────────────────
    @Override
    public boolean add(Object... obj) {
        return addDonor(
            (String)  obj[0],
            (int)     obj[1],
            (String)  obj[2],
            (String)  obj[3],
            (String)  obj[4],
            (String)  obj[5]
        );
    }

    // ── UPDATE donor phone and email ───────────────────────────────────────
    public boolean updateDonor(int donorId, String newPhone, String newEmail) {
        String sql = "UPDATE Donor SET Phone = ?, Email = ? WHERE DonorID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPhone);
            ps.setString(2, newEmail);
            ps.setInt(3, donorId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating donor: " + e.getMessage());
        } finally {
            System.out.println("updateDonor() attempted for ID: " + donorId);
        }
        return false;
    }

    // ── DELETE donor ───────────────────────────────────────────────────────
    public boolean deleteDonor(int donorId) {
        String sql = "DELETE FROM Donor WHERE DonorID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, donorId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error deleting donor: " + e.getMessage());
        }
        return false;
    }

    // ── SELECT all donors ──────────────────────────────────────────────────
    public List<String[]> getAllDonors() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Donor";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("DonorID"),
                    rs.getString("Name"),
                    String.valueOf(rs.getInt("Age")),
                    rs.getString("BloodGroup"),
                    rs.getString("Phone"),
                    rs.getString("Email")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error fetching donors: " + e.getMessage());
        }
        return list;
    }

    // ── SEARCH by blood group ──────────────────────────────────────────────
    public List<String[]> searchByBloodGroup(String bg) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Donor WHERE BloodGroup = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bg);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("DonorID"),
                    rs.getString("Name"),
                    String.valueOf(rs.getInt("Age")),
                    rs.getString("BloodGroup"),
                    rs.getString("Phone"),
                    rs.getString("Email")
                });
            }

        } catch (SQLException e) {
            System.out.println("Error searching donors: " + e.getMessage());
        }
        return list;
    }
}