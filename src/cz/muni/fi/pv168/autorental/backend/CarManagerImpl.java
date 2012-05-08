package cz.muni.fi.pv168.autorental.backend;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import java.util.ArrayList;

import cz.muni.fi.pv168.common.*;

public class CarManagerImpl implements CarManager {

    private DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(CarManagerImpl.class.getName());
    
    public CarManagerImpl(DataSource dataSource) {
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
    public void addCar(Car car) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Adding car...");
	checkDataSource();
	validate(car);
	if (car.getId() != null) {
	    String msg = "Car ID is already used";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("INSERT INTO cars (model, plate, fee) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
	    st.setString(1, car.getModel());
	    st.setString(2, car.getPlate());
	    st.setBigDecimal(3, car.getFee());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, car, true);
	    Long id = DBUtils.getId(st.getGeneratedKeys());
            car.setId(id);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Adding car failed";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
    
    /* Final - Strajk */
    @Override
    public void updateCar(Car car) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Updating car...");
	checkDataSource();
	validate(car);
	if (car.getId() == null) {
	    String msg = "Car ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalEntityException(msg);
	}
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("UPDATE cars SET model = ?, plate = ?, fee = ? WHERE id = ?");
	    st.setString(1, car.getModel());
	    st.setString(2, car.getPlate());
	    st.setBigDecimal(3, car.getFee());
	    st.setLong(4, car.getId());

	    int count = st.executeUpdate();
	    DBUtils.checkUpdatesCount(count, car, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Updating car failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }
    
    /* Final - Strajk */
    @Override
    public void removeCar(Car car) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Removing car...");
	checkDataSource();
	if (car == null) {
            throw new IllegalArgumentException("Car is null");
        }        
        if (car.getId() == null) {
            throw new IllegalEntityException("Car ID is null");
        }        

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("DELETE FROM cars WHERE id = ?");
	    st.setLong(1, car.getId());
	    
	    int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, car, false);
            conn.commit();
	} catch (SQLException ex) {
	    String msg = "Removing car failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    @Override
    public Car findCarById(Long id) throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Finding car...");
	checkDataSource();
	if (id == null) {
	    String msg = "Car ID is null";
	    LOGGER.log(Level.SEVERE, msg);
	    throw new IllegalArgumentException(msg);
	}

	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("SELECT id, model, plate, fee FROM cars WHERE id = ?");
	    st.setLong(1, id);

	    ResultSet rs = st.executeQuery();
	    if (rs.next()) {
		Car car = rowToCar(rs);
		if (rs.next()) {
		    throw new ServiceFailureException("More cars with same ID");
		}
		conn.commit();
		return car;
	    } else {
		return null;
	    }
	} catch (SQLException ex) {
	    String msg = "Finding car failed.";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    @Override
    public List<Car> findAllCars() throws ServiceFailureException {
	LOGGER.log(Level.INFO, "Finding all cars...");
	checkDataSource();
	
	Connection conn = null;
	PreparedStatement st = null;
	try {
	    conn = dataSource.getConnection();
	    conn.setAutoCommit(false); // Temporary turn autocommit mode off.
	    st = conn.prepareStatement("SELECT id, model, plate, fee FROM cars");
	    
	    ResultSet rs = st.executeQuery();
	    List<Car> list = new ArrayList<Car>();
	    while (rs.next()) {
		list.add(rowToCar(rs));
	    }
	    conn.commit();
	    return list;
	} catch (SQLException ex) {
	    String msg = "Finding all cars failed";
	    LOGGER.log(Level.SEVERE, msg, ex);
	    throw new ServiceFailureException(msg, ex);
	} finally {
	    DBUtils.closeQuietly(conn, st);
	}
    }

    /* Final - Strajk */
    private Car rowToCar(ResultSet rs) throws SQLException {
	Car car = new Car();
	car.setId(rs.getLong(1));
	car.setModel(rs.getString(2));
	car.setPlate(rs.getString(3));
	car.setFee(rs.getBigDecimal(4));
	return car;
    }
    
    /* Final - Strajk */
    /* Helpers */
    static private void validate(Car car) {        
        if (car == null) {
            throw new IllegalArgumentException("Car is null.");
        }
	if (car.getModel().isEmpty()) {
            throw new ValidationException("Model is empty");
        }
        if (car.getPlate().isEmpty()) {
            throw new ValidationException("Plate is empty");
        }
    }
}
