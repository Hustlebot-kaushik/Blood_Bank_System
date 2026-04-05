package bloodbank.model;

public class Donor extends Person {
    private int donorId;
    private int age;
    private String bloodGroup;

    public Donor(int id, String name, int age, String bloodGroup, String phone) {
        super(name, phone);
        this.donorId = id;
        this.age = age;
        this.bloodGroup = bloodGroup;
    }

    public int getDonorId() { return donorId; }
    public int getAge() { return age; }
    public String getBloodGroup() { return bloodGroup; }
}