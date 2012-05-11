package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Rent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RentTableModel extends AbstractTableModel {
 
    private List<Rent> rents = new ArrayList<Rent>();
    private static enum COLUMNS {
        ID, CUSTOMER, CAR, FROM, TO, COST
    }
 
    @Override
    public int getRowCount() {
        return rents.size();
    }
 
    @Override
    public int getColumnCount() {
        return COLUMNS.values().length;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		return Integer.class;
	    case CUSTOMER:
	    case CAR:
		return String.class;
	    case FROM:
	    case TO:
		return Date.class;
	    case COST:
		return BigDecimal.class;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public String getColumnName(int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_id");
	    case CUSTOMER:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_customer");
	    case CAR:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_car");
	    case FROM:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_from");
	    case TO:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_to");
	    case COST:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("rents_table_cost");
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Rent rent = rents.get(rowIndex);
        switch (COLUMNS.values()[columnIndex]) {
            case ID:
                return rent.getId();
            case CUSTOMER:
                return rent.getCustomer();
            case CAR:
                return rent.getCar();
	    case FROM:
                return rent.getFrom();
            case TO:
                return rent.getTo();
	    case COST:
                return rent.getCost();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addRent(Rent rent) {
	rents.add(rent);
	int lastRow = rents.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void removeRent(Rent rent) {
	rents.remove(rent);
	int lastRow = rents.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void clear() {
	rents.clear();
        fireTableDataChanged();
    }
    
     public List<Rent> getAllRents() {
	return rents;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Rent rent = rents.get(rowIndex);
	switch (COLUMNS.values()[columnIndex]) {
	    case FROM:
		rent.setFrom(Date.valueOf((String) aValue));
		break;
	    case TO:
		rent.setTo(Date.valueOf((String) aValue));
		break;
	    case COST:
		rent.setCost(new BigDecimal((String) aValue));
		break;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
	fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
	    case CUSTOMER:
	    case CAR:
		return false;
	    case FROM:
	    case TO:
	    case COST:
		return true;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }

}
