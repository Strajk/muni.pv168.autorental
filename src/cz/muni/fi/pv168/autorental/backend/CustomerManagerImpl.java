package cz.muni.fi.pv168.autorental.backend;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import java.util.ArrayList;

import cz.muni.fi.pv168.common.*;

public class CustomerManagerImpl implements CustomerManager {

    private DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(CustomerManagerImpl.class.getName());
    
    public CustomerManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /* Final - Strajk */
    private void checkDataSource() {
	if (dataSource == null) {
            String msg = "Data source is not set";
            LOGGER.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg);
        }
    }

    /* Final - Strajk */
    @Override
    public void addCustomer(Customer customer) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Adding customer...");
	checkDataSource();
	validate(customer);
	if (customer.getId() != null) {
	    String msg = "Customer ID is already used";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("INSERT INTO customers (firstname, lastname, birth, email) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	    st.setString(1, customer.getFirstname());
	    st.setString(2, customer.getLastname());
	    st.setDate(3, customer.getBirth());
	    st.setString(4, customer.getEmail());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, customer, true);
	    Long id = DBUtils.getId(st.getGeneratedKeys());
            customer.setId(id);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Adding customer failed";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
    
    /* Final - Strajk */
    @Override
    public void updateCustomer(Customer customer) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Updating customer...");
	checkDataSource();
	validate(customer);
	if (customer.getId() == null) {
	    String msg = "Customer ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("UPDATE customers SET firstname = ?, lastname = ?, birth = ?, email = ? WHERE id = ?");
	    st.setString(1, customer.getFirstname());
	    st.setString(2, customer.getLastname());
	    st.setDate(3, customer.getBirth());
	    st.setString(4, customer.getEmail());
	    st.setLong(5, customer.getId());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, customer, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Updating customer failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
    
    /* Final - Strajk */
    @Override
    public void removeCustomer(Customer customer) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Removing customer...");
	checkDataSource();
	if (customer == null) {
            throw new IllegalArgumentException("Customer is null");
        }        
        if (customer.getId() == null) {
            throw new IllegalEntityException("Customer ID is null");
        }        

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("DELETE FROM customers WHERE id = ?");
	    st.setLong(1, customer.getId());
	    
	    int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, customer, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Removing customer failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    @Override
    public Customer findCustomerById(Long id) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Finding customer...");
	checkDataSource();
	if (id == null) {
	    String msg = "Customer ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalArgumentException(msg);
	}

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("SELECT id, firstname, lastname, birth, email FROM customers WHERE id = ?");
	    st.setLong(1, id);

	    ResultSet rs = st.executeQuery();
	    if (rs.next()) {
		Customer customer = rowToCustomer(rs);
		if (rs.next()) {
		    throw new ServiceFailureException("More customers with same ID");
		}
		conn.commit();
		return customer;
	    } else {
		return null;
	    }
	} catch (SQLException ex) {
	    String msg = "Finding customer failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    @Override
    public List<Customer> findAllCustomers() throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Finding all customers...");
	checkDataSource();
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("SELECT id, firstname, lastname, birth, email FROM customers");
	    
	    ResultSet rs = st.executeQuery();
	    List<Customer> list = new ArrayList<Customer>();
	    while (rs.next()) {
		list.add(rowToCustomer(rs));
	    }
	    conn.commit();
	    return list;
	} catch (SQLException ex) {
	    String msg = "Finding all customers failed";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    private Customer rowToCustomer(ResultSet rs) throws SQLException {
	Customer customer = new Customer();
	customer.setId(rs.getLong(1));
	customer.setFirstname(rs.getString(2));
	customer.setLastname(rs.getString(3));
	customer.setBirth(rs.getDate(4));
	customer.setEmail(rs.getString(5));
	return customer;
    }
    
    /* Final - Strajk */
    /* Helpers */
    static private void validate(Customer customer) {        
        if (customer == null) {
            throw new IllegalArgumentException("Customer is null.");
        }
	if (customer.getFirstname() == null) {
            throw new ValidationException("First name is null");
        }
        if (customer.getLastname() == null) {
            throw new ValidationException("Last name is null");
        }
    }
}
