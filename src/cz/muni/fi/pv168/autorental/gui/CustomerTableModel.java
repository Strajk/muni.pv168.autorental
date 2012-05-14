package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Customer;
import cz.muni.fi.pv168.autorental.backend.CustomerManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class CustomerTableModel extends AbstractTableModel {
    
    private static final Logger LOGGER = Logger.getLogger(CustomerTableModel.class.getName());
    private CustomerManager customerManager;
    private List<Customer> customers = new ArrayList<Customer>();
    private static enum COLUMNS {
        ID, FIRSTNAME, LASTNAME, BIRTH, EMAIL
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
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
	fireTableDataChanged();
    }
        
    public void removeCustomer(Customer customer) {
	customers.remove(customer);
	fireTableDataChanged();
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
	switch (columnIndex) {
	    case 0:
		customer.setId((Long) aValue);
		break;
	    case 1:
		customer.setFirstname((String) aValue);
		break;
	    case 2:
		customer.setLastname((String) aValue);
		break;
	    case 3:
		customer.setBirth(Date.valueOf((String) aValue));
		break;
            case 4:
		customer.setEmail((String) aValue);
		break;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
        try {
            customerManager.updateCustomer(customer);
            fireTableDataChanged();
        } catch (Exception ex) {
            String msg = "User request failed";
            LOGGER.log(Level.INFO, msg);
        }
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
