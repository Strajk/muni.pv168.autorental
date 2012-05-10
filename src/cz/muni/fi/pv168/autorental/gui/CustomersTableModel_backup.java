/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Customer;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

public class CustomersTableModel_backup extends AbstractTableModel {
    
    private List<Customer> customers = new ArrayList<Customer>();
    private ResourceBundle localization;
    
    public CustomersTableModel_backup(List<Customer> customers, ResourceBundle localization) {
        this.customers.addAll(customers); 
        this.localization = localization;
    }

    @Override
    public int getRowCount() {
	return customers.size();
    }

    @Override
    public int getColumnCount() {
	 return 5;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch (columnIndex) {
	    case 0: // ID
		return Integer.class;
	    case 1: // Firstname
		return String.class;
	    case 2: // Lastname
		return String.class;
	    case 3: // Birth
		return Date.class;
	    case 4: // Email
		return String.class;
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }
    
    @Override
    public String getColumnName(int columnIndex) {
	switch (columnIndex) {
	    case 0:
		return localization.getString("id");
	    case 1:
		return localization.getString("car_model");
	    case 2:
		return localization.getString("car_plate");
	    case 3:
		return localization.getString("car_fee");
	    default:
		throw new IllegalArgumentException("columnIndex");
	}
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getId();
            case 1:
                return customer.getFirstname();
            case 2:
                return customer.getLastname();
            case 3:
                return customer.getBirth();
	    case 4:
                return customer.getEmail();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    public List<Customer> getTableData(){
        return Collections.unmodifiableList(customers);
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
    
    @Override
    public void setValueAt(Object obj, int rowIndex, int columnIndex){
        if(rowIndex > customers.size())
            throw new IllegalArgumentException("Row index greater than customers size");
        Customer c = customers.get(rowIndex); 
        switch(columnIndex){
            case 1:
		c.setFirstname((String) obj);
                break;
            case 2:
		c.setLastname((String) obj);
                break;
            case 3:
		c.setBirth((Date) obj);
                break;
	    case 4:
		c.setEmail((String) obj);
                break;
            default: throw new IllegalArgumentException("column index");
        }
    }
    
    void add(Customer customer) {
        customers.add(customer);
        fireTableDataChanged();
    }
    
   
}
