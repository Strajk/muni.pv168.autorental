package cz.muni.fi.pv168.autorental.backend;

import java.util.List;

/**
 * This entity represents CarManager.
 * Used to CRUD operations with Car
 * 
 */
public interface CarManager {
    public void addCar(Car car);
    public void removeCar(Car car);
    public void updateCar(Car car);
    public Car findCarById(Long id);
    public List<Car> findAllCars();
}
