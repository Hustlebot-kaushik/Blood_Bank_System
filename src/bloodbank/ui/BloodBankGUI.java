package bloodbank.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bloodbank.dao.BloodUnitDAO;
import bloodbank.dao.DonorDAO;
import bloodbank.dao.HospitalDAO;

import java.awt.*;
import java.util.List;

public class BloodBankGUI extends JFrame {

    private DonorDAO      donorDAO      = new DonorDAO();
    private BloodUnitDAO  bloodUnitDAO  = new BloodUnitDAO();
    private HospitalDAO   hospitalDAO   = new HospitalDAO();

    private DefaultTableModel donorModel, inventoryModel,
                               requestModel, hospitalModel;

    public BloodBankGUI() {
        setTitle("Blood Bank Management System");
        setSize(1050, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title bar
        JLabel titleBar = new JLabel(
            "  Blood Bank Management System", SwingConstants.CENTER);
        titleBar.setFont(new Font("Arial", Font.BOLD, 22));
        titleBar.setForeground(Color.WHITE);
        titleBar.setBackground(new Color(146, 43, 33));
        titleBar.setOpaque(true);
        titleBar.setPreferredSize(new Dimension(0, 60));
        add(titleBar, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.addTab("Donors",    createDonorPanel());
        tabs.addTab("Inventory", createInventoryPanel());
        tabs.addTab("Hospitals", createHospitalPanel());
        tabs.addTab("Requests",  createRequestPanel());
        add(tabs, BorderLayout.CENTER);

        // Status bar
        JLabel status = new JLabel(
            "  Blood Bank Management System | SIT Pune");
        status.setFont(new Font("Arial", Font.PLAIN, 11));
        status.setForeground(Color.GRAY);
        status.setPreferredSize(new Dimension(0, 25));
        add(status, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ── TAB 1: DONORS ────────────────────────────────────────────
    private JPanel createDonorPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Register New Donor"));

        JTextField tfName    = new JTextField();
        JTextField tfAge     = new JTextField();
        JTextField tfPhone   = new JTextField();
        JTextField tfEmail   = new JTextField();
        JTextField tfAddress = new JTextField();
        JComboBox<String> cbBG = new JComboBox<>(
            new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-"});

        form.add(new JLabel("Name:"));        form.add(tfName);
        form.add(new JLabel("Age:"));         form.add(tfAge);
        form.add(new JLabel("Blood Group:")); form.add(cbBG);
        form.add(new JLabel("Phone:"));       form.add(tfPhone);
        form.add(new JLabel("Email:"));       form.add(tfEmail);
        form.add(new JLabel("Address:"));     form.add(tfAddress);

        JButton btnAdd    = makeBtn("Add Donor",       new Color(146, 43, 33));
        JButton btnDelete = makeBtn("Delete Selected", new Color(80, 80, 80));
        JButton btnSearch = makeBtn("Search by BG",    new Color(25, 95, 165));
        JButton btnRefresh= makeBtn("Refresh",         new Color(39, 110, 74));

        donorModel = new DefaultTableModel(
            new String[]{"ID","Name","Age","Blood Group","Phone","Email"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(donorModel);

        btnAdd.addActionListener(e -> {
            try {
                boolean ok = donorDAO.addDonor(
                    tfName.getText(),
                    Integer.parseInt(tfAge.getText()),
                    (String) cbBG.getSelectedItem(),
                    tfPhone.getText(),
                    tfEmail.getText(),
                    tfAddress.getText());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Donor added!");
                    clearFields(tfName, tfAge, tfPhone, tfEmail, tfAddress);
                    loadDonors();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number!");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = Integer.parseInt(
                    donorModel.getValueAt(row, 0).toString());
                donorDAO.deleteDonor(id);
                JOptionPane.showMessageDialog(this, "Donor deleted!");
                loadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row first!");
            }
        });

        btnSearch.addActionListener(e -> {
            donorModel.setRowCount(0);
            donorDAO.searchByBloodGroup(
                (String) cbBG.getSelectedItem())
                .forEach(donorModel::addRow);
        });

        btnRefresh.addActionListener(e -> loadDonors());

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btnP.add(btnAdd); btnP.add(btnDelete);
        btnP.add(btnSearch); btnP.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnP, BorderLayout.SOUTH);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        loadDonors();
        return p;
    }

    private void loadDonors() {
        donorModel.setRowCount(0);
        donorDAO.getAllDonors().forEach(donorModel::addRow);
    }

    // ── TAB 2: INVENTORY ─────────────────────────────────────────
    private JPanel createInventoryPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add Blood Unit"));

        JTextField tfDonorId    = new JTextField();
        JTextField tfCollDate   = new JTextField("2025-01-01");
        JTextField tfExpiryDate = new JTextField("2025-04-01");
        JComboBox<String> cbBG  = new JComboBox<>(
            new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-"});

        form.add(new JLabel("Blood Group:"));              form.add(cbBG);
        form.add(new JLabel("Donor ID:"));                 form.add(tfDonorId);
        form.add(new JLabel("Collection Date (YYYY-MM-DD):")); form.add(tfCollDate);
        form.add(new JLabel("Expiry Date (YYYY-MM-DD):"));     form.add(tfExpiryDate);

        JButton btnAdd     = makeBtn("Add Blood Unit", new Color(146, 43, 33));
        JButton btnRefresh = makeBtn("Refresh",        new Color(39, 110, 74));

        inventoryModel = new DefaultTableModel(
            new String[]{"Blood Group","Total Units","Available Units"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(inventoryModel);

        btnAdd.addActionListener(e -> {
            try {
                bloodUnitDAO.addBloodUnit(
                    (String) cbBG.getSelectedItem(),
                    tfCollDate.getText(),
                    tfExpiryDate.getText(),
                    Integer.parseInt(tfDonorId.getText()));
                JOptionPane.showMessageDialog(this,
                    "Blood unit added! Inventory updated automatically.");
                loadInventory();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Donor ID must be a number!");
            }
        });

        btnRefresh.addActionListener(e -> loadInventory());

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btnP.add(btnAdd); btnP.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnP, BorderLayout.SOUTH);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        loadInventory();
        return p;
    }

    private void loadInventory() {
        inventoryModel.setRowCount(0);
        bloodUnitDAO.getInventorySummary().forEach(inventoryModel::addRow);
    }

    // ── TAB 3: HOSPITALS ─────────────────────────────────────────
    private JPanel createHospitalPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Add Hospital"));

        JTextField tfName    = new JTextField();
        JTextField tfPhone   = new JTextField();
        JTextField tfContact = new JTextField();

        form.add(new JLabel("Hospital Name:"));  form.add(tfName);
        form.add(new JLabel("Phone:"));          form.add(tfPhone);
        form.add(new JLabel("Contact Person:")); form.add(tfContact);
        form.add(new JLabel(""));                form.add(new JLabel(""));

        JButton btnAdd     = makeBtn("Add Hospital", new Color(146, 43, 33));
        JButton btnRefresh = makeBtn("Refresh",      new Color(39, 110, 74));

        hospitalModel = new DefaultTableModel(
            new String[]{"ID","Name","Phone","Contact Person"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(hospitalModel);

        btnAdd.addActionListener(e -> {
            hospitalDAO.addHospital(
                tfName.getText(),
                tfPhone.getText(),
                tfContact.getText());
            JOptionPane.showMessageDialog(this, "Hospital added!");
            clearFields(tfName, tfPhone, tfContact);
            loadHospitals();
        });

        btnRefresh.addActionListener(e -> loadHospitals());

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btnP.add(btnAdd); btnP.add(btnRefresh);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnP, BorderLayout.SOUTH);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        loadHospitals();
        return p;
    }

    private void loadHospitals() {
        hospitalModel.setRowCount(0);
        hospitalDAO.getAllHospitals().forEach(hospitalModel::addRow);
    }

    // ── TAB 4: REQUESTS ──────────────────────────────────────────
    private JPanel createRequestPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(2, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder(
            "Submit / Fulfill Blood Request"));

        JTextField tfHospitalId = new JTextField();
        JTextField tfUnits      = new JTextField();
        JTextField tfReqId      = new JTextField();
        JTextField tfStaffId    = new JTextField("1");
        JComboBox<String> cbBG  = new JComboBox<>(
            new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-"});

        form.add(new JLabel("Hospital ID:"));          form.add(tfHospitalId);
        form.add(new JLabel("Blood Group:"));          form.add(cbBG);
        form.add(new JLabel("Units Needed:"));         form.add(tfUnits);
        form.add(new JLabel("Request ID (fulfill):")); form.add(tfReqId);

        JButton btnSubmit  = makeBtn("Submit Request",  new Color(146, 43, 33));
        JButton btnFulfill = makeBtn("Fulfill Request", new Color(25, 95, 165));
        JButton btnRefresh = makeBtn("Refresh",         new Color(39, 110, 74));

        requestModel = new DefaultTableModel(
            new String[]{"ID","Hospital","Blood Group",
                          "Units","Status","Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = styledTable(requestModel);

        btnSubmit.addActionListener(e -> {
            try {
                hospitalDAO.addBloodRequest(
                    (String) cbBG.getSelectedItem(),
                    Integer.parseInt(tfUnits.getText()),
                    Integer.parseInt(tfHospitalId.getText()));
                JOptionPane.showMessageDialog(this, "Request submitted!");
                loadRequests();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
            }
        });

        btnFulfill.addActionListener(e -> {
            try {
                boolean ok = hospitalDAO.fulfillRequest(
                    Integer.parseInt(tfReqId.getText()),
                    Integer.parseInt(tfStaffId.getText()));
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                        "Request fulfilled! Blood unit marked Used.");
                    loadRequests();
                    loadInventory();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Enter a valid Request ID!");
            }
        });

        btnRefresh.addActionListener(e -> loadRequests());

        JPanel btnP = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        btnP.add(btnSubmit); btnP.add(btnFulfill); btnP.add(btnRefresh);
        btnP.add(new JLabel("  Staff ID:")); btnP.add(tfStaffId);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnP, BorderLayout.SOUTH);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        loadRequests();
        return p;
    }

    private void loadRequests() {
        requestModel.setRowCount(0);
        hospitalDAO.getAllRequests().forEach(requestModel::addRow);
    }

    // ── HELPERS ───────────────────────────────────────────────────
    private JButton makeBtn(String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.PLAIN, 12));
        return b;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(26);
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.getTableHeader().setBackground(new Color(146, 43, 33));
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        t.setSelectionBackground(new Color(220, 180, 180));
        return t;
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    // ── MAIN ──────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BloodBankGUI::new);
    }
}