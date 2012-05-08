/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.helpers;

import cz.muni.fi.pv168.autorental.backend.Car;
import cz.muni.fi.pv168.autorental.backend.Customer;
import cz.muni.fi.pv168.autorental.backend.Rent;
import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author strajk
 */
public class Constructors {
    
    public static Car newCar(String model, String plate, BigDecimal fee) {
        Car car = new Car();
        car.setModel(model);
        car.setPlate(plate);
        car.setFee(fee);
        return car;
    }
    
    public static Customer newCustomer(String fisrtName, String lastName, Date birth, String email) {
        Customer customer = new Customer();
        customer.setFirstname(fisrtName);
        customer.setLastname(lastName);
        customer.setBirth(birth);
        customer.setEmail(email);
        return customer;
    }
    
    public static Rent newRent(Customer customer, Car car, Date from, Date to, BigDecimal cost) {
        Rent rent = new Rent();
        rent.setCar(car);
        rent.setCustomer(customer);
        rent.setFrom(from);
        rent.setTo(to);
        rent.setCost(cost);
        return rent;
    }
    
}
