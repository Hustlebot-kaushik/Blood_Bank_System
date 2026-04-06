package bloodbank.model;

/**
 * Hospital — extends Person (Inheritance).
 * Overrides toString() demonstrating Polymorphism (Method Overriding).
 */
public class Hospital extends Person {

    private int hospitalId;

    public Hospital(int id, String name, String phone) {
        super(name, phone);
        this.hospitalId = id;
    }

    public int getHospitalId() { return hospitalId; }

    @Override
    public String toString() {
        return "Hospital[id=" + hospitalId + ", name=" + name + ", phone=" + phone + "]";
    }
}