package bloodbank.model;

/**
 * Person — base class for Donor and Hospital.
 * Demonstrates: Inheritance (OOPs principle).
 */
public class Person {

    protected String name;
    protected String phone;

    public Person(String name, String phone) {
        this.name  = name;
        this.phone = phone;
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }

    // toString — overridden by subclasses (Polymorphism: Method Overriding)
    @Override
    public String toString() {
        return "Person[name=" + name + ", phone=" + phone + "]";
    }
}