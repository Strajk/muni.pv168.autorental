package cz.muni.fi.pv168.autorental.backend;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import java.util.ArrayList;

import cz.muni.fi.pv168.common.*;
import java.util.Collections;

public class RentManagerImpl implements RentManager {

    private static final String DB_SELECT = "SELECT rents.id AS id, rents.customer_id, rents.car_id, rents.date_from, rents.date_to, rents.cost " +
			    ", customers.firstname, customers.lastname, customers.birth, customers.email " +
			    ", cars.model, cars.plate, cars.fee " +
			    "FROM rents " +
			    "JOIN customers ON rents.customer_id = customers.id " +
			    "JOIN cars ON rents.car_id = cars.id ";

    private DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(RentManagerImpl.class.getName());
    
    public RentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void checkDataSource() {
	if (dataSource == null) {
            String msg = "Data source is not set";
            LOGGER.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public void addRent(Rent rent) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Adding rent...");
	checkDataSource();
	if (rent.getId() != null) {
	    String msg = "Rent ID is already used";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	validate(rent);
	rent.setCost(rent.calculateCost());
        
        Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("INSERT INTO rents (customer_id, car_id, date_from, date_to, cost) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	    st.setLong(1, rent.getCustomer().getId());
            st.setLong(2, rent.getCar().getId());
            st.setDate(3, rent.getFrom());
            st.setDate(4, rent.getTo());
            st.setBigDecimal(5, rent.getCost());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, rent, true);
	    Long id = DBUtils.getId(st.getGeneratedKeys());
            rent.setId(id);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Adding rent failed";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
    
    /* Final - Strajk */
    @Override
    public void updateRent(Rent rent) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Updating rent...");
	checkDataSource();
	if (rent.getId() == null) {
	    String msg = "Rent ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	validate(rent);
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("UPDATE rents SET customer_id = ?, car_id = ?, date_from = ?, date_to = ?, cost = ? WHERE ID = ?");
	    st.setLong(1, rent.getCustomer().getId());
            st.setLong(2, rent.getCar().getId());
            st.setDate(3, rent.getFrom()); 
            st.setDate(4, rent.getTo());
            st.setBigDecimal(5, rent.getCost());
            st.setLong(6, rent.getId());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, rent, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Updating rent failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    @Override
    public void removeRent(Rent rent) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Removing rent...");
	checkDataSource();
	if (rent == null) {
            throw new IllegalArgumentException("Rent is null");
        }        
        if (rent.getId() == null) {
            throw new IllegalEntityException("Rent ID is null");
        }        

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("DELETE FROM rents WHERE id = ?");
	    st.setLong(1, rent.getId());
	    
	    int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, rent, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Removing rent failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
   
    /* Final - Strajk */
    @Override
    public Rent findRentById(Long id) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Finding rent by id...");
	checkDataSource();
	if (id == null) {
	    String msg = "Rent ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalArgumentException(msg);
	}

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement(
			DB_SELECT + " WHERE rents.id = ?");
	    st.setLong(1, id);

	    ResultSet rs = st.executeQuery();
	    if (rs.next()) {
		Rent rent = rowToRent(rs);
		if (rs.next()) {
		    throw new ServiceFailureException("More rents with same ID");
		}
		conn.commit();
		return rent;
	    } else {
		return null;
	    }
	} catch (SQLException ex) {
	    String msg = "Finding rent failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
	    DBUtils.closeQuietly(conn, st);
	}
    }

    @Override
    public List<Rent> findAllRents() {
	LOGGER.log(Level.INFO, "Finding all rents...");
	checkDataSource();
        Connection conn = null;
	PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
			DB_SELECT);
            ResultSet rs = st.executeQuery();
            List<Rent> result = new ArrayList<Rent>();
            while (rs.next()) { 
                result.add(rowToRent(rs));
            }
            conn.commit();
            return Collections.unmodifiableList(result);
        } catch (SQLException ex) {
            String msg = "Finding all rents failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
        } finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn);
        }
    }

    @Override
    public List<Rent> findAllCustomerRents(Customer customer) {
	LOGGER.log(Level.INFO, "Finding all customer rents...");
	if (customer.getId() == null) {
            throw new IllegalEntityException("Customer ID is null");
        }  
	checkDataSource();
        Connection conn = null;
	PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
			DB_SELECT + " WHERE customer_id = ?");
	    st.setLong(1, customer.getId());
            ResultSet rs = st.executeQuery();
            List<Rent> result = new ArrayList<Rent>();
            while (rs.next()) { 
                result.add(rowToRent(rs));
            }
            conn.commit();
            return Collections.unmodifiableList(result);
        } catch (SQLException ex) {
            String msg = "Finding all customer rents failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
        } finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn);
        }
    }

    @Override
    public List<Rent> findAllCarRents(Car car) {
	LOGGER.log(Level.INFO, "Finding all car rents...");
	if (car.getId() == null) {
            throw new IllegalEntityException("Car ID is null");
        }  
	checkDataSource();
        Connection conn = null;
	PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
			DB_SELECT + " WHERE cars.id = ?");
	    st.setLong(1, car.getId());
            ResultSet rs = st.executeQuery();
            List<Rent> result = new ArrayList<Rent>();
            while (rs.next()) { 
                result.add(rowToRent(rs));
            }
            conn.commit();
            return Collections.unmodifiableList(result);
        } catch (SQLException ex) {
            String msg = "Finding all car rents failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
        } finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn);
        }
    }    
    
    /* Final - Strajk */
    /* Helpers */
    static private void validate(Rent rent) {        
        if (rent == null) {
            throw new IllegalArgumentException("Customer is null.");
        }
	if (rent.getFrom() == null) {
            throw new ValidationException("Date from is null");
        }
        if (rent.getTo() == null) {
            throw new ValidationException("Date to is null");
        }
	if (rent.getTo().before(rent.getFrom())) {
            throw new ValidationException("Date to (" + rent.getTo() +"), before Date from (" + rent.getFrom() + ")");
        }
    }

    private static Rent rowToRent(ResultSet rs) throws SQLException {
	Customer customer = new Customer();
	customer.setId(rs.getLong("customer_id"));
	customer.setFirstname(rs.getString("firstname"));
	customer.setLastname(rs.getString("lastname"));
	customer.setBirth(rs.getDate("birth"));
	customer.setEmail(rs.getString("email"));
	
	Car car = new Car();
	car.setId(rs.getLong("car_id"));
	car.setModel(rs.getString("model"));
	car.setPlate(rs.getString("plate"));
	car.setFee(rs.getBigDecimal("fee"));
	
	Rent rent = new Rent();
        rent.setId(rs.getLong("id"));
        rent.setCustomer(customer);
        rent.setCar(car);
        rent.setFrom(rs.getDate("date_from"));
        rent.setTo(rs.getDate("date_to"));
	rent.setCost(rs.getBigDecimal("cost"));
	
        return rent;
	
	
    }

    @Override
    public List<Rent> findAllPresentRents() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Rent> findAllPastRents() {
	throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
