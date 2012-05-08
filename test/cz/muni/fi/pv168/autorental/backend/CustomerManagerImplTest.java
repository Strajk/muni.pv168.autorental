package cz.muni.fi.pv168.autorental.backend;

import cz.muni.fi.pv168.autorental.helpers.Constructors;
import cz.muni.fi.pv168.common.DBUtils;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ladislav
 */
public class CustomerManagerImplTest {

    private static final Logger LOGGER = Logger.getLogger(CustomerManagerImpl.class.getName());
    private CustomerManager customerManager;
    private DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby:memory:autorental-test;create=true");
        return ds;
    }
    
    @Before
    public void setUp() throws SQLException {

        dataSource = prepareDataSource();
        DBUtils.executeSqlScript(dataSource,CustomerManager.class.getResource("db_create.sql"));
	LOGGER.log(Level.INFO, "Testing in-memory database created.");
        customerManager = new CustomerManagerImpl(dataSource);
    }
    
    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, CustomerManager.class.getResource("db_drop.sql"));
	LOGGER.log(Level.INFO, "Testing in-memory database dropped.");
    }
    

    /**
     * Test of addCustomer method, of class CustomerManagerImpl.
     */
    @Test
    public void testAddCustomer() {
        
        try {
            customerManager.addCustomer(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try {
            Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
            customer.setId(new Long(11));
            customerManager.addCustomer(customer);
            // pridavame existujiciho zakaznika
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        // pouzit lze DateFormat - private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US) --- Date date = dateFormat.parse(mm/dd/yyyy);
        Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
        assertNull(customer.getId()); // nesmi byt v databazi
        customerManager.addCustomer(customer);
        assertNotNull(customer.getId()); // musi byt v databazi
        Customer result = customerManager.findCustomerById(customer.getId());
        assertNotNull(result);
        
        assertEquals("Martin", result.getFirstname());
        assertEquals("Vohínek", result.getLastname());
        assertEquals(customer.getBirth(), result.getBirth());
        assertEquals("Martin@seznam.cz", result.getEmail());
        assertEquals(customer, result);
    }

    /**
     * Test of removeCustomer method, of class CustomerManagerImpl.
     */
    @Test
    public void testRemoveCustomer() {
        Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
        Customer customer2 = Constructors.newCustomer("Ondra", "Polášek", Date.valueOf("1987-12-13"), "Ondra@seznam.cz");
        
        try {
            customerManager.removeCustomer(null);
            // zakaznik musi existovat
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try {
            customer.setId(null);
            customerManager.removeCustomer(customer);
            // zakaznik neni v databazi
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        customerManager.addCustomer(customer2);
        assertNotNull(customerManager.findCustomerById(customer2.getId()));

        customerManager.removeCustomer(customer2);
        
        assertNull(customerManager.findCustomerById(customer2.getId()));
        
    }

    /**
     * Test of updateCustomer method, of class CustomerManagerImpl.
     */
    @Test
    public void testUpdateCustomer() { 
        Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
        Customer customer2 = Constructors.newCustomer("Ondra", "Polášek", Date.valueOf("1987-12-13"), "Ondra@seznam.cz");
        
        try {
            customerManager.updateCustomer(null);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        try {
            customer.setId(null);
            // musime aktualizovat existujiciho zakaznika
            customerManager.updateCustomer(customer);
            fail();
        } catch (Exception ex) {
            //OK
        }
        
        customerManager.addCustomer(customer2);
        Long customerId = customer2.getId();
        
        customer2.setFirstname("Lukáš");
        customer2.setLastname("Maňásek");
        customer2.setBirth(Date.valueOf("1985-10-30"));
        customer2.setEmail("Lukas@seznam.cz");
        
        customerManager.updateCustomer(customer2);
        
        Customer result = customerManager.findCustomerById(customerId);
        
        assertEquals("Lukáš", result.getFirstname());
        assertEquals("Maňásek", result.getLastname());
        assertEquals(Date.valueOf("1985-10-30"), result.getBirth());
        assertEquals("Lukas@seznam.cz", result.getEmail());
        assertEquals(customer2, result);
        
        
        
    }

    /**
     * Test of findCustomerById method, of class CustomerManagerImpl.
     */
    @Test
    public void testFindCustomerById() {
        Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
        
        customerManager.addCustomer(customer);
        
        Long customerId = customer.getId();
        assertNotNull(customerId);
        Customer result = customerManager.findCustomerById(customerId);
        assertNotNull(result);
        assertEquals("Martin", result.getFirstname());
        assertEquals("Vohínek", result.getLastname());
        assertEquals(Date.valueOf("1988-05-15"), result.getBirth());
        assertEquals("Martin@seznam.cz", result.getEmail());
        assertEquals(customer, result);
    }

    /**
     * Test of findAllCustomers method, of class CustomerManagerImpl.
     */
    @Test
    public void testFindAllCustomers() {
        Connection conn;
        PreparedStatement st = null;
        
        Customer customer = Constructors.newCustomer("Martin", "Vohínek", Date.valueOf("1988-05-15"), "Martin@seznam.cz");
        Customer customer2 = Constructors.newCustomer("Ondra", "Polášek", Date.valueOf("1987-12-13"), "Ondra@seznam.cz");
        
        customerManager.addCustomer(customer);
        customerManager.addCustomer(customer2);
        
        List<Customer> expected = Arrays.asList(customer,customer2);
        List<Customer> unmodifiableActualList  = customerManager.findAllCustomers();
        
        List<Customer> actual = new ArrayList<Customer>(unmodifiableActualList);


        Collections.sort(actual,idComparator);
        Collections.sort(expected,idComparator);  

        
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }
    
   
     private static Comparator<Customer> idComparator = new Comparator<Customer>() {

        @Override
        public int compare(Customer o1, Customer o2) {
            return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
        }
    };
             
    private void assertDeepEquals(List<Customer> expectedList, List<Customer> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Customer expected = expectedList.get(i);
            Customer actual = actualList.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getFirstname(), actual.getFirstname());
            assertEquals(expected.getLastname(), actual.getLastname());
            assertEquals(expected.getBirth(), actual.getBirth());
            assertEquals(expected.getEmail(), actual.getEmail());
        }
    }
    /*
    private class CustomerComparator implements Comparator{
        public int compare(Object cus1, Object cus2){    
            if ((cus1 instanceof  Customer) && (cus2 instanceof  Customer)) {
                Customer customer1 = (Customer)cus1;
                Customer customer2 = (Customer)cus2;
                return customer1.getId().compareTo(customer2.getId());
            } else {
                return 0;
            }
        }
    }*/
}
