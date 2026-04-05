package bloodbank.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bloodbank.util.DBConnection;

public class HospitalDAO implements DAO {

    public boolean addHospital(String name, String phone, String contact) {
        String sql = "INSERT INTO Hospital(Name, Phone, ContactPerson) VALUES(?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, contact);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        } finally {
            System.out.println("Add Hospital attempted");
        }
        return false;
    }

    @Override
    public boolean add(Object... obj) {
        return addHospital(
            (String) obj[0],
            (String) obj[1],
            (String) obj[2]
        );
    }

    public List<String[]> getAllHospitals() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Hospital";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("HospitalID"),
                    rs.getString("Name"),
                    rs.getString("Phone"),
                    rs.getString("ContactPerson")
                });
            }

        } catch (SQLException e) {
            System.out.println("Fetch hospitals failed: " + e.getMessage());
        }
        return list;
    }

    public boolean addBloodRequest(String bloodGroup, int units, int hospitalId) {
        String sql = "INSERT INTO BloodRequest(BloodGroup, UnitsRequired, RequestDate, Status, HospitalID) VALUES(?, ?, CURDATE(), 'Pending', ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bloodGroup);
            ps.setInt(2, units);
            ps.setInt(3, hospitalId);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Request failed: " + e.getMessage());
        }
        return false;
    }

    public List<String[]> getAllRequests() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT BR.RequestID, H.Name, BR.BloodGroup, BR.UnitsRequired, BR.Status, BR.RequestDate FROM BloodRequest BR JOIN Hospital H ON BR.HospitalID = H.HospitalID";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("RequestID"),
                    rs.getString("Name"),
                    rs.getString("BloodGroup"),
                    rs.getString("UnitsRequired"),
                    rs.getString("Status"),
                    rs.getString("RequestDate")
                });
            }

        } catch (SQLException e) {
            System.out.println("Fetch requests failed: " + e.getMessage());
        }
        return list;
    }

    public boolean fulfillRequest(int requestId, int staffId) {
        String sql = "{CALL FulfillRequest(?, ?)}";

        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, requestId);
            cs.setInt(2, staffId);

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Fulfill failed: " + e.getMessage());
        }
        return false;
    }
}