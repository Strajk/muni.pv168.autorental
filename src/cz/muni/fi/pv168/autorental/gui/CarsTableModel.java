/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Car;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author strajk
 */
public class CarsTableModel extends AbstractTableModel {
 
    private List<Car> cars = new ArrayList<Car>();
 
    @Override
    public int getRowCount() {
        return cars.size();
    }
 
    @Override
    public int getColumnCount() {
        return 5;
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
	    case 4:
		return String.class;
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
	int lastRow = cars.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
    
    @Override
    public String getColumnName(int columnIndex) {
	switch (columnIndex) {
	    case 0:
		return "Id"; // Internacionalizace  v TableCellRenderer 
	    case 1:
		return "Model";
	    case 2:
		return "Plate";
	    case 3:
		return "Fee";
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Car car = cars.get(rowIndex);
	switch (columnIndex) {
	    case 0:
		car.setId((Long) aValue);
		break;
	    case 1:
		car.setModel((String) aValue);
		break;
	    case 2:
		car.setPlate((String) aValue);
		break;
	    case 3:
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
