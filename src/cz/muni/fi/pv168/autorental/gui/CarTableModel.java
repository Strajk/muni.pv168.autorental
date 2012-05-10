package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Car;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CarTableModel extends AbstractTableModel {
 
    private List<Car> cars = new ArrayList<Car>();
    private static enum COLUMNS {
        ID, MODEL, PLATE, FEE
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
		return Integer.class;
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
        switch (COLUMNS.values()[columnIndex]) {
            case ID:
                return car.getId();
            case MODEL:
                return car.getModel();
            case PLATE:
                return car.getPlate();
            case FEE:
                return car.getFee();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCar(Car car) {
	cars.add(car);
	int lastRow = cars.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void removeCar(Car car) {
	cars.remove(car);
	int lastRow = cars.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
     public List<Car> getAllCars() {
	return cars;
    }

    
    @Override
    public String getColumnName(int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		return "id";
	    case MODEL:
		return "model";
	    case PLATE:
		return "plate";
	    case FEE:
		return "fee";
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Car car = cars.get(rowIndex);
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		car.setId((Long) aValue);
		break;
	    case MODEL:
		car.setModel((String) aValue);
		break;
	    case PLATE:
		car.setPlate((String) aValue);
		break;
	    case FEE:
		car.setFee(new BigDecimal((String)aValue));
		break;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
	fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	switch (columnIndex) {
	    case 1:
	    case 2:
		return true;
	    case 0:
	    case 3:
	    case 4:
		return false;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }

}
