package bloodbank.dao;

/**
 * DAO interface — defines the contract for all Data Access Objects.
 * Demonstrates: Interface usage (OOPs principle).
 */
public interface DAO {
    /**
     * Generic add operation. Each implementing class maps its own
     * typed parameters through this single varargs signature,
     * demonstrating Polymorphism (Method Overriding).
     */
    boolean add(Object... obj);
}