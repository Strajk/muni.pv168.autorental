package cz.muni.fi.pv168.autorental.backend;

import cz.muni.fi.pv168.autorental.helpers.Constructors;
import cz.muni.fi.pv168.autorental.helpers.Sampler;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

public class Autorental {
    
    private static final Logger LOGGER = Logger.getLogger(Autorental.class.getName());
    private Context context;
    private DataSource dataSource;
    
    public static void main(String[] args) {
        Autorental app = new Autorental();
	
        try {
            app.setUp();
        } catch (Exception ex) {
	    String msg = "Application setup failed.";
            LOGGER.log(Level.SEVERE, msg, ex);
        }
	
	
	// app.addSomeCars();
	// app.addSomeCustomers();
	app.addSomeRents();
    }
    
    private void addSomeCars() {
	Car trabant = Constructors.newCar("Trabant", "ABC 11 22", new BigDecimal(100));
	Car brouk = Constructors.newCar("VW Beetle", "BCC 22 33", new BigDecimal(200));
	Car oktavka = Constructors.newCar("Skoda Octavia", "CDD 33 44", new BigDecimal(300));
	Car porshe = Constructors.newCar("Porshe 911", "ZZZ 99 99", new BigDecimal(999));
	
	CarManager carManager = new CarManagerImpl(dataSource);
	carManager.addCar(trabant);
	carManager.addCar(brouk);
	carManager.addCar(oktavka);
	carManager.addCar(porshe);
	for(int i = 0; i <= 10; i++) {
	    carManager.addCar(Sampler.createSampleCar());
	}
    }
    
    private void addSomeCustomers() {
	Customer anicka = Constructors.newCustomer("Anicka", "Amend", Date.valueOf("1990-01-11"), "anicka@example.com");
	Customer bobik = Constructors.newCustomer("Bobik", "Baculaty", Date.valueOf("1992-02-12"), "bobik@example.com");
	Customer cecilka = Constructors.newCustomer("Cecilka", "Cudna", Date.valueOf("1994-03-13"), "cecilka@example.com");
	Customer david = Constructors.newCustomer("David", "Dovedny", Date.valueOf("1996-04-14"), "david@example.com");
	
	CustomerManager customerManager = new CustomerManagerImpl(dataSource);
	customerManager.addCustomer(anicka);
	customerManager.addCustomer(bobik);
	customerManager.addCustomer(cecilka);
	customerManager.addCustomer(david);
	for(int i = 0; i <= 10; i++) {
	    customerManager.addCustomer(Sampler.createSampleCustomer());
	}
	
    }
    
    private void addSomeRents() {
	Random random = new Random();
	
	RentManager rentManager = new RentManagerImpl(dataSource);
	CustomerManager customerManager = new CustomerManagerImpl(dataSource);
	CarManager carManager = new CarManagerImpl(dataSource);
	
	List<Customer> customers = customerManager.findAllCustomers();
	List<Car> cars = carManager.findAllCars();
	
	Rent rent;
	for(int i = 0; i <= 20; i++) {
	    rent = new Rent();
	    rent.setCustomer(customers.get(random.nextInt(customers.size())));
	    rent.setCar(cars.get(random.nextInt(cars.size())));
	    rent.setFrom(Date.valueOf("2010-01-01"));
	    rent.setTo(Date.valueOf("2010-02-02"));
	    rent.setCost(rent.calculateCost());

	    rentManager.addRent(rent);
	}
    }
    
    
    private void setUp() throws Exception {
	
	BasicDataSource bds = new BasicDataSource();
	bds.setUrl("jdbc:derby://localhost:1527/autorental");
	bds.setUsername("app");
	bds.setPassword("app");
	dataSource = bds;
	
    }
    
    
    
//    private void setUp() throws Exception {
//        
//	try {
//            Class.forName("org.derby.jdbc.Driver");
//        } catch (ClassNotFoundException ex) {
//            String msg = "JDBC Drivers registration problem";
//            LOGGER.log(Level.SEVERE, msg, ex);
//            throw new Exception(msg, ex);
//        }
//        
//        try {
//            context    = (Context) new InitialContext().lookup("java:comp/env");
//            dataSource = (DataSource) context.lookup("jdbc/Autorental");
//            LOGGER.log(Level.INFO, context.getEnvironment().toString());
//        } catch (NamingException ex) {
//            String msg = "DataSource obtaining from Context problem";
//            LOGGER.log(Level.SEVERE, msg, ex);
//            throw new Exception(msg, ex);
//        }
//        
//        try {
//            Connection connection = dataSource.getConnection();
//        } catch (SQLException ex) {
//            String msg = "Error when connecting to DB";
//            LOGGER.log(Level.SEVERE, msg, ex);
//            throw new Exception(msg, ex);
//        }
//    }

}
