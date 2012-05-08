package cz.muni.fi.pv168.autorental.backend;

import cz.muni.fi.pv168.autorental.helpers.Constructors;
import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.common.IllegalEntityException;
import java.util.Collections;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class CarManagerImplTest {
    
    private static final Logger LOGGER = Logger.getLogger(CarManagerImpl.class.getName());
    private CarManager carManager;
    private DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:autorental-test;create=true");
	LOGGER.log(Level.INFO, "Data Source prepared.");
        return dataSource;
    }

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        DBUtils.executeSqlScript(dataSource, CarManager.class.getResource("db_create.sql"));
	LOGGER.log(Level.INFO, "Testing in-memory database created.");
        carManager = new CarManagerImpl(dataSource);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, CarManager.class.getResource("db_drop.sql"));
	LOGGER.log(Level.INFO, "Testing in-memory database dropped.");
    }
    
    @Test
    public void testAddCar() {
        
        try {
            carManager.addCar(null); // Car that is to be added has to exists
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try { // ID already set
            Car car = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
	    car.setId(Long.valueOf(33));
            carManager.addCar(car);
            fail();
        } catch (Exception ex) {
            //OK
        }
	
	try { // Empty model
	    Car car = Constructors.newCar("", "1A1 1122", new BigDecimal("150"));
            carManager.addCar(car);
            fail();
        } catch (Exception ex) {
            //OK
        }
	
	try { // Empty plate
	    Car car = Constructors.newCar("Skoda Felicia", "", new BigDecimal("150"));
            carManager.addCar(car);
            fail();
        } catch (Exception ex) {
            //OK
        }
	
	// Generating id
	Car car = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
	carManager.addCar(car);
	Long carId = car.getId();
	assertNotNull(carId);
	
	// Returning same car by same id
	Car carReturned = carManager.findCarById(carId);
	assertEquals(car, carReturned);
	assertDeepEquals(car, carReturned);

    }
    
    /**
     * Test of removeCustomer method, of class CustomerManagerImpl.
     */
    @Test
    public void testRemoveCar() {
        
	try { // Removing null car
            carManager.removeCar(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try { // Removing car without id
            Car car = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
            carManager.removeCar(car);
            fail();
        } catch (Exception ex) {
            //OK
        }
       
	// Add sample cars
	Car felicia = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
	Car fabia = Constructors.newCar("Skoda Fabia", "1B1 1122", new BigDecimal("200"));
	Car octavia = Constructors.newCar("Skoda Octavia", "1C1 1122", new BigDecimal("300"));
        carManager.addCar(felicia);
        carManager.addCar(fabia);
        carManager.addCar(octavia);
	
	assertNotNull(carManager.findCarById(fabia.getId()));
        carManager.removeCar(fabia);
        assertNull(carManager.findCarById(fabia.getId()));
        
    }

    @Test
    public void testUpdateCar() {
        
        try { // Cannot update null
            carManager.updateCar(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try { // Cannot update car without id
            Car car = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
            carManager.updateCar(car);
            fail();
        } catch (Exception ex) {
            //OK
        }
	
	Car felicia = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
        Car fabia = Constructors.newCar("Skoda Fabia", "2B2 1234", new BigDecimal("200"));
        Car octavia = Constructors.newCar("Skoda Octavia", "1B2 6598", new BigDecimal("320"));
        
        carManager.addCar(felicia);
        carManager.addCar(fabia);
	
        Long felicia_id = felicia.getId();
	assertNotNull(felicia_id);
        
        Car testedCar = carManager.findCarById(felicia_id);
        assertEquals(testedCar.getFee(), new BigDecimal(150).setScale(2));
        testedCar.setFee(new BigDecimal("180"));
        carManager.updateCar(testedCar);
        
        Car testedCar2 = carManager.findCarById(felicia_id);
        assertEquals(testedCar2.getFee(), new BigDecimal("180").setScale(2));
                
        // Check if updates didn"t affected other records
        assertEquals(fabia, carManager.findCarById(fabia.getId()));
    }
    
    @Test
    public void testFindCustomerById() {
        System.out.println("Test: CarManager.findCarById()");
        
        Car felicia = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
        
        carManager.addCar(felicia);
        
        Long carId = felicia.getId();
        assertNotNull(carId);
        Car result = carManager.findCarById(carId);
        assertNotNull(result);

        assertDeepEquals(felicia, result);
        assertEquals(felicia, result);
    }

   
    @Test
    public void testFindAllCars() {
         System.out.println("Test: CarManager.findAllCars()");
        
        assertTrue(carManager.findAllCars().isEmpty());
        
        Car felicia = Constructors.newCar("Skoda Felicia", "1A1 1122", new BigDecimal("150"));
        Car fabia = Constructors.newCar("Skoda Fabia", "2B2 1234", new BigDecimal("200"));
        Car octavia = Constructors.newCar("Skoda Octavia", "3C3 1122", new BigDecimal("300"));
        Car superb = Constructors.newCar("Skoda Superb", "4D4 1122", new BigDecimal("500"));
        
        carManager.addCar(felicia);
        carManager.addCar(fabia);
        carManager.addCar(octavia);
        carManager.addCar(superb);
        
        List<Car> expected = Arrays.asList(felicia, fabia, octavia, superb);
        List<Car> returned = carManager.findAllCars();
        
        Collections.sort(expected, idComparator);
        Collections.sort(returned, idComparator);
        
        assertEquals(expected, returned);
        assertDeepEquals(expected, returned);
    }
    
    private void assertDeepEquals(Car expected, Car actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getPlate(), actual.getPlate());
        assertEquals(expected.getFee().setScale(2), actual.getFee().setScale(2));
    }
    
    private void assertDeepEquals(List<Car> expectedList, List<Car> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Car expected = expectedList.get(i);
            Car actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }
    
    private static Comparator<Car> idComparator = new Comparator<Car>() {

        @Override
        public int compare(Car o1, Car o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
}