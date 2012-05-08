package cz.muni.fi.pv168.autorental.backend;


import java.util.List;

/**
 * This entity represents CarManager.
 * Used to CRUD operations with Car
 * 
 */
public interface CustomerManager {
    public void addCustomer(Customer customer);
    public void removeCustomer(Customer customer);
    public void updateCustomer(Customer customer);
    public Customer findCustomerById(Long id);
    public  List<Customer> findAllCustomers(); 
}
