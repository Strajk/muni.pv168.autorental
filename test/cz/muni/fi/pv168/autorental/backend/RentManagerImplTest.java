package cz.muni.fi.pv168.autorental.backend;

import cz.muni.fi.pv168.autorental.helpers.Constructors;
import cz.muni.fi.pv168.autorental.helpers.Sampler;
import cz.muni.fi.pv168.common.DBUtils;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RentManagerImplTest {
    
    private static final Logger LOGGER = Logger.getLogger(CustomerManagerImpl.class.getName());
    private RentManager rentManager;
    private CustomerManager customerManager;
    private CarManager carManager;
    private DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:autorental-test;create=true");
        return ds;
    }
    
    @Before
    public void setUp() throws SQLException {
	
        dataSource = prepareDataSource();
        DBUtils.executeSqlScript(dataSource, RentManager.class.getResource("db_create.sql"));
        rentManager = new RentManagerImpl(dataSource);
	customerManager = new CustomerManagerImpl(dataSource);
	carManager = new CarManagerImpl(dataSource);
        
        Customer customer1 = Sampler.createSampleCustomer();
        Customer customer2 = Sampler.createSampleCustomer();
        Customer customer3 = Sampler.createSampleCustomer();
	customerManager.addCustomer(customer1);
	customerManager.addCustomer(customer2);
	customerManager.addCustomer(customer3);
        Car car1 = Sampler.createSampleCar();
        Car car2 = Sampler.createSampleCar();
        Car car3 = Sampler.createSampleCar();
	carManager.addCar(car1);
	carManager.addCar(car2);
	carManager.addCar(car3);
        
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, RentManager.class.getResource("db_drop.sql"));
    }

    /**
     * Test of addRent method, of class RentManagerImpl.
     */
    @Test
    public void testAddRent() {
	
        try {
            rentManager.addRent(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        customerManager = new CustomerManagerImpl(dataSource);
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
	LOGGER.log(Level.INFO, "Testing adding rent with customer: {0}", customer.toString());
        carManager = new CarManagerImpl(dataSource);
        Car car = carManager.findCarById(Long.valueOf(1));
	LOGGER.log(Level.INFO, "Testing adding rent with car: {0}", car.toString());
        
        try { // Existing Rent ID
            Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-01-01"), Date.valueOf("2012-01-31"), null);
	    rent.setCost(rent.calculateCost());
            rent.setId(new Long(33));
            rentManager.addRent(rent);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-01-01"), Date.valueOf("2012-01-31"), null);
	rent.setCost(rent.calculateCost());
	rentManager.addRent(rent);
	
        assertNotNull(rent.getId());
        Rent result = rentManager.findRentById(rent.getId());
        assertNotNull(result);
        
        assertEquals(rent, result);
        assertDeepEquals(rent, result);
    }


    @Test
    public void testRemoveRent() {
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        Car car = carManager.findCarById(Long.valueOf(1));
        
        try {
            rentManager.removeRent(null);
            // zakaznik musi existovat
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try {
            Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
            rent.setId(null);
            rentManager.removeRent(rent);
            // zakaznik neni v databazi
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        rentManager.addRent(rent);
        assertNotNull(rentManager.findRentById(rent.getId()));

        rentManager.removeRent(rent);
        
        assertNull(rentManager.findRentById(rent.getId()));
    }


    @Test
    public void testUpdateRent() {
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        Customer customer2 = customerManager.findCustomerById(Long.valueOf(2));
        Car car = carManager.findCarById(Long.valueOf(1));
        Car car2 = carManager.findCarById(Long.valueOf(2));
        
        try { // Whole rent not set
            rentManager.updateRent(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try { // ID not set
            Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-01-01"), Date.valueOf("2012-01-30"), null);
            rent.setId(null);
            rentManager.updateRent(rent);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-01-01"), Date.valueOf("2012-01-30"), null);
        rentManager.addRent(rent);
        Long rentId = rent.getId();
	assertNotNull(rentId);
        
        rent.setCustomer(customer2);
        rent.setCar(car2);
        rent.setFrom(Date.valueOf("2012-03-01"));
        rent.setTo(Date.valueOf("2013-03-30"));
        rent.setCost(rent.getCost().add(new BigDecimal(100)));
        
        rentManager.updateRent(rent);
        
        Rent result = rentManager.findRentById(rentId);
        
        assertEquals(rent, result);
        assertDeepEquals(rent, result);
    }


    @Test
    public void testFindRentById() {
        CustomerManagerImpl customerManager = new CustomerManagerImpl(dataSource);
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        CarManagerImpl carManager = new CarManagerImpl(dataSource);
        Car car = carManager.findCarById(Long.valueOf(1));
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        
        rentManager.addRent(rent);
        
        Long rentId = rent.getId();
        assertNotNull(rentId);
        Rent result = rentManager.findRentById(rentId);
        assertNotNull(result);
        
        assertEquals(rent, result);
        assertDeepEquals(rent, result);
    }


    @Test
    public void testFindAllRents() {
        
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        Customer customer2 = customerManager.findCustomerById(Long.valueOf(2));
        Car car = carManager.findCarById(Long.valueOf(1));
        Car car2 = carManager.findCarById(Long.valueOf(2));
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        Rent rent2 = Constructors.newRent(customer2, car2, Date.valueOf("2012-06-25"), Date.valueOf("2012-06-30"), null);
        
        rentManager.addRent(rent);
        rentManager.addRent(rent2);
        
        List<Rent> expected = Arrays.asList(rent,rent2);
        List<Rent> unmodifiableActualList  = rentManager.findAllRents();
        
        List<Rent> actual = new ArrayList<Rent>(unmodifiableActualList);


        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator);  

        
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }


    @Test
    public void testFindAllCustomerRents() {
        
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        Customer customer2 = customerManager.findCustomerById(Long.valueOf(2));
        Car car = carManager.findCarById(Long.valueOf(1));
        Car car2 = carManager.findCarById(Long.valueOf(2));
        Car car3 = carManager.findCarById(Long.valueOf(3));
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        Rent rent2 = Constructors.newRent(customer2, car2, Date.valueOf("2012-02-15"), Date.valueOf("2012-02-18"), null);
        Rent rent3 = Constructors.newRent(customer2, car3, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        
        rentManager.addRent(rent);
        rentManager.addRent(rent2);
        rentManager.addRent(rent3);
        
        List<Rent> expected = Arrays.asList(rent2,rent3);
        List<Rent> unmodifiableActualList  = rentManager.findAllCustomerRents(customer2);
        
        List<Rent> actual = new ArrayList<Rent>(unmodifiableActualList);

        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator); 
        
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }


    @Test
    public void testFindAllCarRents() {
  
        Customer customer = customerManager.findCustomerById(Long.valueOf(1));
        Customer customer2 = customerManager.findCustomerById(Long.valueOf(2));
        Customer customer3 = customerManager.findCustomerById(Long.valueOf(3));
        Car car = carManager.findCarById(Long.valueOf(1));
        Car car2 = carManager.findCarById(Long.valueOf(2));
        
        Rent rent = Constructors.newRent(customer, car, Date.valueOf("2012-05-15"), Date.valueOf("2012-05-18"), null);
        Rent rent3 = Constructors.newRent(customer3, car, Date.valueOf("2012-06-02"), Date.valueOf("2012-07-12"), null);
        Rent rent2 = Constructors.newRent(customer2, car2, Date.valueOf("2012-06-25"), Date.valueOf("2012-06-30"), null);
        
        rentManager.addRent(rent);
        rentManager.addRent(rent2);
        rentManager.addRent(rent3);
        
        List<Rent> expected = Arrays.asList(rent,rent3);
        List<Rent> unmodifiableActualList  = rentManager.findAllCarRents(car);
        
        List<Rent> actual = new ArrayList<Rent>(unmodifiableActualList);

        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator); 
        
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    
    
    
     private static Comparator<Rent> idComparator = new Comparator<Rent>() {

        @Override
        public int compare(Rent o1, Rent o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
             
    private void assertDeepEquals(List<Rent> expectedList, List<Rent> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Rent expected = expectedList.get(i);
            Rent actual = actualList.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getCar(), actual.getCar());
            assertEquals(expected.getCustomer(), actual.getCustomer());
            assertEquals(expected.getFrom(), actual.getFrom());
            assertEquals(expected.getTo(), actual.getTo());
            assertEquals(expected.getCost(), actual.getCost());
        }
    }
    
    private void assertDeepEquals(Rent expected, Rent actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCar(), actual.getCar());
        assertEquals(expected.getCustomer(), actual.getCustomer());
        assertEquals(expected.getFrom(), actual.getFrom());
        assertEquals(expected.getTo(), actual.getTo());
        assertEquals(expected.getCost(), actual.getCost());
    }
   
    
}
