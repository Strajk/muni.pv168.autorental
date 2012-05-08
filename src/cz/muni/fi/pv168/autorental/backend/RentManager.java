package cz.muni.fi.pv168.autorental.backend;

import java.util.List;

/**
 * This entity represents CarManager.
 * Used to CRUD operations with Car
 * 
 */
public interface RentManager {
    public void addRent(Rent rent);
    public void removeRent(Rent rent);
    public void updateRent(Rent rent);
    public Rent findRentById(Long id);
    public List<Rent> findAllRents();
    
    public List<Rent> findAllCustomerRents(Customer customer);
    public List<Rent> findAllCarRents(Car car);
    
    public List<Rent> findAllPresentRents();
    public List<Rent> findAllPastRents();
}
