package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Car;
import cz.muni.fi.pv168.autorental.backend.CarManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class CarTableModel extends AbstractTableModel {
    
    private static final Logger LOGGER = Logger.getLogger(CarTableModel.class.getName());
    private CarManager carManager;
    private List<Car> cars = new ArrayList<Car>();
    private static enum COLUMNS {
        ID, MODEL, PLATE, FEE
    }
    
    public void setCarManager(CarManager carManager) {
        this.carManager = carManager;
    }
 
    @Override
    public int getRowCount() {
        return cars.size();
    }
 
    @Override
    public int getColumnCount() {
        return COLUMNS.values().length;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch (columnIndex) {
	    case 0:
		return Long.class;
	    case 1:
	    case 2:
		return String.class;
	    case 3:
		return BigDecimal.class;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car car = cars.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return car.getId();
            case 1:
                return car.getModel();
            case 2:
                return car.getPlate();
            case 3:
                return car.getFee();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCar(Car car) {
	cars.add(car);
	fireTableDataChanged();
    }
    
    public void removeCar(Car car) {
	cars.remove(car);
	fireTableDataChanged();
    }
    
    public void clear() {
	cars.clear();
        fireTableDataChanged();
    }
    
     public List<Car> getAllCars() {
	return cars;
    }

    
    @Override
    public String getColumnName(int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("cars_table_id");
	    case MODEL:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("cars_table_model");
	    case PLATE:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("cars_table_plate");
	    case FEE:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("cars_table_fee");
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Car car = cars.get(rowIndex);
	switch (COLUMNS.values()[columnIndex]) {
	    case MODEL:
		car.setModel((String) aValue);
		break;
	    case PLATE:
		car.setPlate((String) aValue);
		break;
	    case FEE:
		car.setFee((BigDecimal) aValue);
		break;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
        try {
            carManager.updateCar(car);
            fireTableDataChanged();
        } catch (Exception ex) {
            String msg = "User request failed";
            LOGGER.log(Level.INFO, msg);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	switch (columnIndex) {
	    case 1:
	    case 2:
            case 3:
		return true;
	    case 0:
		return false;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }

}
