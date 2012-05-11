package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Customer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class CustomerTableModel extends AbstractTableModel {
 
    private List<Customer> customers = new ArrayList<Customer>();
    private static enum COLUMNS {
        ID, FIRSTNAME, LASTNAME, BIRTH, EMAIL
    }
 
    @Override
    public int getRowCount() {
        return customers.size();
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
	    case FIRSTNAME:
	    case LASTNAME:
	    case EMAIL:
		return String.class;
	    case BIRTH:
		return Date.class;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public String getColumnName(int columnIndex) {
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("customers_table_id");
	    case FIRSTNAME:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("customers_table_firstname");
	    case LASTNAME:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("customers_table_lastname");
	    case BIRTH:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("customers_table_birth");
	    case EMAIL:
		return java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("customers_table_email");
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);
        switch (COLUMNS.values()[columnIndex]) {
            case ID:
                return customer.getId();
            case FIRSTNAME:
                return customer.getFirstname();
            case LASTNAME:
                return customer.getLastname();
	    case BIRTH:
                return customer.getBirth();
            case EMAIL:
                return customer.getEmail();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public void addCustomer(Customer customer) {
	customers.add(customer);
	int lastRow = customers.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
        
    public void removeCustomer(Customer customer) {
	customers.remove(customer);
	int lastRow = customers.size() - 1;
	fireTableRowsInserted(lastRow, lastRow);
    }
    
    public void clear() {
	customers.clear();
        fireTableDataChanged();
    }
    
     public List<Customer> getAllCustomers() {
	return customers;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Customer customer = customers.get(rowIndex);
	switch (COLUMNS.values()[columnIndex]) {
	    case ID:
		customer.setId((Long) aValue);
		break;
	    case FIRSTNAME:
		customer.setFirstname((String) aValue);
		break;
	    case LASTNAME:
		customer.setLastname((String) aValue);
		break;
	    case BIRTH:
		customer.setBirth(Date.valueOf((String) aValue));
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
		return false;
	    case FIRSTNAME:
	    case LASTNAME:
	    case BIRTH:
	    case EMAIL:
		return true;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }

}
