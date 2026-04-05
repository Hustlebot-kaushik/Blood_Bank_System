package bloodbank.model;

public class Hospital extends Person {
    private int hospitalId;

    public Hospital(int id, String name, String phone) {
        super(name, phone);
        this.hospitalId = id;
    }

    public int getHospitalId() { return hospitalId; }
}