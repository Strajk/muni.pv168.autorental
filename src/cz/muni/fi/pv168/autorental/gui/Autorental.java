/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author strajkaaaa
 */
public class Autorental extends javax.swing.JFrame {

    BasicDataSource basicDataSource = new BasicDataSource();
    private static final Logger LOGGER = Logger.getLogger(Autorental.class.getName());
    private String action;
    
    RentManager rentManager;
    CustomerManager customerManager;
    CarManager carManager;
    
    RentTableModel rentTableModel;
    CustomerTableModel customerTableModel;
    CarTableModel carTableModel;
    
    private RentsSwingWorker rentsSwingWorker;
    private class RentsSwingWorker extends SwingWorker<Void, Rent> {

	@Override
	protected Void doInBackground() throws Exception {
	    rentTableModel = (RentTableModel) rents_table.getModel();
            rentTableModel.setRentManager(rentManager);
            int counter = 0;
	    for (Rent rent : rentManager.findAllRents()) {
                counter++;
                Thread.sleep(70);
		publish(rent);
                setProgress(counter);
	    }
	    return null;
	}
	
	@Override
	protected void process(List<Rent> items) {
	    for (Rent i : items) {
                rentTableModel.addRent(i);
	    }
	}

        @Override
        protected void done() {
            rents_load.setEnabled(true);
            rents_progress.setValue(100);
            rentsSwingWorker = null;
        }
    }
    private CustomersSwingWorker customersSwingWorker;
    private class CustomersSwingWorker extends SwingWorker<Void, Customer> {

	@Override
	protected Void doInBackground() throws Exception {
	    customerTableModel = (CustomerTableModel) customers_table.getModel();
            customerTableModel.setCustomerManager(customerManager);
            int counter = 0;
	    for (Customer customer : customerManager.findAllCustomers()) {
                counter++;
                Thread.sleep(50);
		publish(customer);
                setProgress(counter);
	    }
	    return null;
	}
	
	@Override
	protected void process(List<Customer> items) {
	    for (Customer i : items) {
                customerTableModel.addCustomer(i);
	    }
	}

        @Override
        protected void done() {
            customers_load.setEnabled(true);
            customers_progress.setValue(100);
            customersSwingWorker = null;
        }
    }
    private CarsSwingWorker carsSwingWorker;
    private class CarsSwingWorker extends SwingWorker<Void, Car> {

	@Override
	protected Void doInBackground() throws Exception {
	    carTableModel = (CarTableModel) cars_table.getModel();
            carTableModel.setCarManager(carManager);
            int counter = 0;
	    for (Car car : carManager.findAllCars()) {
                counter++;
                Thread.sleep(80);
		publish(car);
                setProgress(counter);
	    }
	    return null;
	}
	
	@Override
	protected void process(List<Car> items) {
	    for (Car i : items) {
                carTableModel.addCar(i);
	    }
	}

        @Override
        protected void done() {
            cars_load.setEnabled(true);
            cars_progress.setValue(100);
            carsSwingWorker = null;
        }
    }
    
    private void setUp() throws Exception {
        Properties configFile = new Properties();
        configFile.load(new FileInputStream("src/config.properties"));
	BasicDataSource bds = new BasicDataSource();
	bds.setUrl( configFile.getProperty( "url" ) );
	bds.setPassword( configFile.getProperty( "password" ) );
	bds.setUsername( configFile.getProperty( "username" ) );
	basicDataSource = bds;
    }

    public Autorental() {
        
        try {
            setUp();
        } catch (Exception ex) {
	    String msg = "Application setup failed.";
            LOGGER.log(Level.SEVERE, msg, ex);
        }
        
        /* Number and date formats */
        /*
        Locale locale_cs = new Locale("cs");
        Locale locale_en = new Locale("en");
        Locale locale_sk = new Locale("sk");
        */
        /*
        NumberFormat locale_cs_numberFormatCurrency = NumberFormat.getCurrencyInstance(locale_cs);
        locale_cs_numberFormatCurrency.setCurrency(Currency.getInstance("Kč"));
        NumberFormat locale_en_numberFormatCurrency = NumberFormat.getCurrencyInstance(locale_en);
        locale_en_numberFormatCurrency.setCurrency(Currency.getInstance("Kč"));
        NumberFormat locale_sk_numberFormatCurrency = NumberFormat.getCurrencyInstance(locale_sk);
        locale_sk_numberFormatCurrency.setCurrency(Currency.getInstance("Kč"));
        
        DateFormat locale_cs_dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale_cs);
        DateFormat locale_en_dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale_cs);
        DateFormat locale_sk_dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale_cs);
        
        ResourceBundle localization = ResourceBundle.getBundle("localization", Locale.getDefault());
        */
        
	initComponents();
	
        rentManager	= new RentManagerImpl(basicDataSource);
        customerManager = new CustomerManagerImpl(basicDataSource);
        carManager	= new CarManagerImpl(basicDataSource);
        
        rentsSwingWorker = new RentsSwingWorker();
        rentsSwingWorker.addPropertyChangeListener(rentsProgressListener);
        rentsSwingWorker.execute();
        
        customersSwingWorker = new CustomersSwingWorker();
        customersSwingWorker.addPropertyChangeListener(customersProgressListener);
        customersSwingWorker.execute();
        
        carsSwingWorker = new CarsSwingWorker();
        carsSwingWorker.addPropertyChangeListener(carsProgressListener);
        carsSwingWorker.execute();
    }
    
    /* SwingWorkers Progress Linsteners */
    private PropertyChangeListener rentsProgressListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("progress")) {
                rents_progress.setValue((Integer) evt.getNewValue());
            }
        }
    };
    
    private PropertyChangeListener customersProgressListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("progress")) {
                customers_progress.setValue((Integer) evt.getNewValue());
            }
        }
    };
    
    private PropertyChangeListener carsProgressListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("progress")) {
                cars_progress.setValue((Integer) evt.getNewValue());
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialog_rents = new javax.swing.JDialog();
        dialog_rents_idLabel = new javax.swing.JLabel();
        dialog_rents_idInput = new javax.swing.JTextField();
        dialog_rents_customerLabel = new javax.swing.JLabel();
        dialog_rents_customerInput = new javax.swing.JTextField();
        dialog_rents_customerHelper = new javax.swing.JLabel();
        dialog_rents_carLabel = new javax.swing.JLabel();
        dialog_rents_carInput = new javax.swing.JTextField();
        dialog_rents_carHelper = new javax.swing.JLabel();
        dialog_rents_fromLabel = new javax.swing.JLabel();
        dialog_rents_fromInput = new javax.swing.JTextField();
        dialog_rents_toLabel = new javax.swing.JLabel();
        dialog_rents_toInput = new javax.swing.JTextField();
        dialog_rents_costLabel = new javax.swing.JLabel();
        dialog_rents_costInput = new javax.swing.JTextField();
        dialog_rents_calculate = new javax.swing.JButton();
        dialog_rents_cancel = new javax.swing.JButton();
        dialog_rents_submit = new javax.swing.JButton();
        dialog_customers = new javax.swing.JDialog();
        dialog_customers_idLabel = new javax.swing.JLabel();
        dialog_customers_idInput = new javax.swing.JTextField();
        dialog_customers_firstnameLabel = new javax.swing.JLabel();
        dialog_customers_firstnameInput = new javax.swing.JTextField();
        dialog_customers_lastnameLabel = new javax.swing.JLabel();
        dialog_customers_lastnameInput = new javax.swing.JTextField();
        dialog_customers_birthLabel = new javax.swing.JLabel();
        dialog_customers_birthInput = new javax.swing.JTextField();
        dialog_customers_emailLabel = new javax.swing.JLabel();
        dialog_customers_emailInput = new javax.swing.JTextField();
        dialog_customers_cancel = new javax.swing.JButton();
        dialog_customers_submit = new javax.swing.JButton();
        dialog_cars = new javax.swing.JDialog();
        dialog_cars_idLabel = new javax.swing.JLabel();
        dialog_cars_idInput = new javax.swing.JTextField();
        dialog_cars_modelLabel = new javax.swing.JLabel();
        dialog_cars_modelInput = new javax.swing.JTextField();
        dialog_cars_plateLabel = new javax.swing.JLabel();
        dialog_cars_plateInput = new javax.swing.JTextField();
        dialog_cars_feeLabel = new javax.swing.JLabel();
        dialog_cars_feeInput = new javax.swing.JTextField();
        dialog_cars_submit = new javax.swing.JButton();
        dialog_cars_cancel = new javax.swing.JButton();
        header = new javax.swing.JPanel();
        header_title = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        rents = new javax.swing.JPanel();
        rents_scroll = new javax.swing.JScrollPane();
        rents_table = new javax.swing.JTable();
        rents_add = new javax.swing.JButton();
        rents_update = new javax.swing.JButton();
        rents_delete = new javax.swing.JButton();
        rents_title = new javax.swing.JLabel();
        rents_load = new javax.swing.JButton();
        rents_progress = new javax.swing.JProgressBar();
        customers = new javax.swing.JPanel();
        customers_scroll = new javax.swing.JScrollPane();
        customers_table = new javax.swing.JTable();
        customers_add = new javax.swing.JButton();
        customers_update = new javax.swing.JButton();
        customers_delete = new javax.swing.JButton();
        customers_title = new javax.swing.JLabel();
        customers_use = new javax.swing.JButton();
        customers_load = new javax.swing.JButton();
        customers_progress = new javax.swing.JProgressBar();
        cars = new javax.swing.JPanel();
        cars_scroll = new javax.swing.JScrollPane();
        cars_table = new javax.swing.JTable();
        cars_add = new javax.swing.JButton();
        cars_update = new javax.swing.JButton();
        cars_delete = new javax.swing.JButton();
        cars_title = new javax.swing.JLabel();
        cars_use = new javax.swing.JButton();
        cars_load = new javax.swing.JButton();
        cars_progress = new javax.swing.JProgressBar();

        dialog_rents.setResizable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle"); // NOI18N
        dialog_rents_idLabel.setText(bundle.getString("Autorental.dialog_rents_idLabel.text")); // NOI18N

        dialog_rents_idInput.setEnabled(false);
        dialog_rents_idInput.setPreferredSize(new java.awt.Dimension(200, 28));
        dialog_rents_idInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_rents_idInputActionPerformed(evt);
            }
        });

        dialog_rents_customerLabel.setText(bundle.getString("Autorental.dialog_rents_customerLabel.text")); // NOI18N

        dialog_rents_customerInput.setEnabled(false);

        dialog_rents_customerHelper.setText(bundle.getString("Autorental.dialog_rents_customerHelper.text")); // NOI18N

        dialog_rents_carLabel.setText(bundle.getString("Autorental.dialog_rents_carLabel.text")); // NOI18N

        dialog_rents_carInput.setEnabled(false);

        dialog_rents_carHelper.setText(bundle.getString("Autorental.dialog_rents_carHelper.text")); // NOI18N

        dialog_rents_fromLabel.setText(bundle.getString("Autorental.dialog_rents_fromLabel.text")); // NOI18N

        dialog_rents_fromInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_rents_toLabel.setText(bundle.getString("Autorental.dialog_rents_toLabel.text")); // NOI18N

        dialog_rents_toInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_rents_costLabel.setText(bundle.getString("Autorental.dialog_rents_costLabel.text")); // NOI18N

        dialog_rents_costInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_rents_calculate.setText(bundle.getString("Autorental.dialog_rents_calculate.text")); // NOI18N
        dialog_rents_calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_rents_calculateActionPerformed(evt);
            }
        });

        dialog_rents_cancel.setText(bundle.getString("Autorental.dialog_rents_cancel.text")); // NOI18N
        dialog_rents_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_rents_cancelActionPerformed(evt);
            }
        });

        dialog_rents_submit.setText(bundle.getString("Autorental.dialog_rents_submit.text")); // NOI18N
        dialog_rents_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_rents_submitActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout dialog_rentsLayout = new org.jdesktop.layout.GroupLayout(dialog_rents.getContentPane());
        dialog_rents.getContentPane().setLayout(dialog_rentsLayout);
        dialog_rentsLayout.setHorizontalGroup(
            dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_rentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rentsLayout.createSequentialGroup()
                        .add(dialog_rents_calculate)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(dialog_rents_cancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dialog_rents_submit))
                    .add(dialog_rentsLayout.createSequentialGroup()
                        .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_costLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_toLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_fromLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_carLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_customerLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_rents_idLabel))
                        .add(12, 12, 12)
                        .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(dialog_rents_fromInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                            .add(dialog_rents_costInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_rents_toInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_rentsLayout.createSequentialGroup()
                                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, dialog_rents_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, dialog_rents_carInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, dialog_rents_customerInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(dialog_rents_customerHelper, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(dialog_rents_carHelper, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        dialog_rentsLayout.setVerticalGroup(
            dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_rentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_idLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_customerLabel)
                    .add(dialog_rents_customerInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_customerHelper))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_carLabel)
                    .add(dialog_rents_carInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_carHelper))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_fromInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_fromLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_toInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_toLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_costInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_rents_costLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_rents_submit)
                    .add(dialog_rents_cancel)
                    .add(dialog_rents_calculate))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialog_customers.setResizable(false);

        dialog_customers_idLabel.setText(bundle.getString("Autorental.dialog_customers_idLabel.text")); // NOI18N

        dialog_customers_idInput.setEnabled(false);
        dialog_customers_idInput.setPreferredSize(new java.awt.Dimension(200, 28));
        dialog_customers_idInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_customers_idInputActionPerformed(evt);
            }
        });

        dialog_customers_firstnameLabel.setText(bundle.getString("Autorental.dialog_customers_firstnameLabel.text")); // NOI18N

        dialog_customers_firstnameInput.setPreferredSize(new java.awt.Dimension(200, 28));
        dialog_customers_firstnameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_customers_firstnameInputActionPerformed(evt);
            }
        });

        dialog_customers_lastnameLabel.setText(bundle.getString("Autorental.dialog_customers_lastnameLabel.text")); // NOI18N

        dialog_customers_lastnameInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_customers_birthLabel.setText(bundle.getString("Autorental.dialog_customers_birthLabel.text")); // NOI18N

        dialog_customers_birthInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_customers_emailLabel.setText(bundle.getString("Autorental.dialog_customers_emailLabel.text")); // NOI18N

        dialog_customers_emailInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_customers_emailInputActionPerformed(evt);
            }
        });

        dialog_customers_cancel.setText(bundle.getString("Autorental.dialog_customers_cancel.text")); // NOI18N
        dialog_customers_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_customers_cancelActionPerformed(evt);
            }
        });

        dialog_customers_submit.setText(bundle.getString("Autorental.dialog_customers_submit.text")); // NOI18N
        dialog_customers_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_customers_submitActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout dialog_customersLayout = new org.jdesktop.layout.GroupLayout(dialog_customers.getContentPane());
        dialog_customers.getContentPane().setLayout(dialog_customersLayout);
        dialog_customersLayout.setHorizontalGroup(
            dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_customersLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(dialog_customersLayout.createSequentialGroup()
                        .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customers_emailLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customers_birthLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customers_lastnameLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customers_firstnameLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customers_idLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(dialog_customers_lastnameInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                            .add(dialog_customers_birthInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_customers_firstnameInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_customers_emailInput)
                            .add(dialog_customersLayout.createSequentialGroup()
                                .add(dialog_customers_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, Short.MAX_VALUE))))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_customersLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(dialog_customers_cancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dialog_customers_submit)))
                .addContainerGap())
        );
        dialog_customersLayout.setVerticalGroup(
            dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_customersLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_idLabel)
                    .add(dialog_customers_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_firstnameLabel)
                    .add(dialog_customers_firstnameInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_lastnameLabel)
                    .add(dialog_customers_lastnameInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_birthLabel)
                    .add(dialog_customers_birthInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_emailInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dialog_customers_emailLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_customers_submit)
                    .add(dialog_customers_cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dialog_cars.setResizable(false);

        dialog_cars_idLabel.setText(bundle.getString("Autorental.dialog_cars_idLabel.text")); // NOI18N

        dialog_cars_idInput.setEnabled(false);
        dialog_cars_idInput.setPreferredSize(new java.awt.Dimension(200, 28));
        dialog_cars_idInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_cars_idInputActionPerformed(evt);
            }
        });

        dialog_cars_modelLabel.setText(bundle.getString("Autorental.dialog_cars_modelLabel.text")); // NOI18N

        dialog_cars_modelInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_cars_plateLabel.setText(bundle.getString("Autorental.dialog_cars_plateLabel.text")); // NOI18N

        dialog_cars_plateInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_cars_feeLabel.setText(bundle.getString("Autorental.dialog_cars_feeLabel.text")); // NOI18N

        dialog_cars_feeInput.setPreferredSize(new java.awt.Dimension(200, 28));

        dialog_cars_submit.setText(bundle.getString("Autorental.dialog_cars_submit.text")); // NOI18N
        dialog_cars_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_cars_submitActionPerformed(evt);
            }
        });

        dialog_cars_cancel.setText(bundle.getString("Autorental.dialog_cars_cancel.text")); // NOI18N
        dialog_cars_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialog_cars_cancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout dialog_carsLayout = new org.jdesktop.layout.GroupLayout(dialog_cars.getContentPane());
        dialog_cars.getContentPane().setLayout(dialog_carsLayout);
        dialog_carsLayout.setHorizontalGroup(
            dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_carsLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_carsLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(dialog_cars_cancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dialog_cars_submit))
                    .add(dialog_carsLayout.createSequentialGroup()
                        .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_cars_modelLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_cars_idLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_cars_plateLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, dialog_cars_feeLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(dialog_cars_modelInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                            .add(dialog_cars_plateInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_cars_feeInput, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(dialog_carsLayout.createSequentialGroup()
                                .add(dialog_cars_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        dialog_carsLayout.setVerticalGroup(
            dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(dialog_carsLayout.createSequentialGroup()
                .addContainerGap()
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_cars_idLabel)
                    .add(dialog_cars_idInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_cars_modelLabel)
                    .add(dialog_cars_modelInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_cars_plateLabel)
                    .add(dialog_cars_plateInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_cars_feeLabel)
                    .add(dialog_cars_feeInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialog_carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dialog_cars_submit)
                    .add(dialog_cars_cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 829));

        header.setPreferredSize(new java.awt.Dimension(1085, 40));

        header_title.setFont(header_title.getFont().deriveFont(header_title.getFont().getStyle() | java.awt.Font.BOLD, header_title.getFont().getSize()+12));
        header_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        header_title.setText(bundle.getString("Autorental.header_title.text")); // NOI18N

        org.jdesktop.layout.GroupLayout headerLayout = new org.jdesktop.layout.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(headerLayout.createSequentialGroup()
                .addContainerGap()
                .add(header_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(header_title, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(26, 26, 26))
        );

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        rents_table.setModel(new RentTableModel());
        rents_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn rents_col_id = rents_table.getColumnModel().getColumn(0);
        rents_col_id.setMaxWidth(40);
        TableColumn rents_col_customer = rents_table.getColumnModel().getColumn(1);
        TableColumn rents_col_car = rents_table.getColumnModel().getColumn(2);
        TableColumn rents_col_from = rents_table.getColumnModel().getColumn(3);
        rents_col_from.setMaxWidth(100);
        TableColumn rents_col_to = rents_table.getColumnModel().getColumn(4);
        rents_col_to.setMaxWidth(100);
        TableColumn rents_col_cost = rents_table.getColumnModel().getColumn(5);
        rents_col_cost.setMaxWidth(100);
        rents_scroll.setViewportView(rents_table);

        rents_add.setText(bundle.getString("Autorental.rents_add.text")); // NOI18N
        rents_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rents_addActionPerformed(evt);
            }
        });

        rents_update.setText(bundle.getString("Autorental.rents_update.text")); // NOI18N
        rents_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rents_updateActionPerformed(evt);
            }
        });

        rents_delete.setText(bundle.getString("Autorental.rents_delete.text")); // NOI18N
        rents_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rents_deleteActionPerformed(evt);
            }
        });

        rents_title.setFont(rents_title.getFont().deriveFont(rents_title.getFont().getStyle() | java.awt.Font.BOLD, rents_title.getFont().getSize()+6));
        rents_title.setText(bundle.getString("Autorental.rents_title.text")); // NOI18N

        rents_load.setText(bundle.getString("Autorental.rents_load.text")); // NOI18N
        rents_load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rents_loadActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout rentsLayout = new org.jdesktop.layout.GroupLayout(rents);
        rents.setLayout(rentsLayout);
        rentsLayout.setHorizontalGroup(
            rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, rentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(rents_scroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(rents_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(rents_update, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(rents_delete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(rents_add, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(rents_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(rents_load, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        rentsLayout.setVerticalGroup(
            rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(rentsLayout.createSequentialGroup()
                .addContainerGap()
                .add(rentsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rentsLayout.createSequentialGroup()
                        .add(rents_title)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rents_add)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rents_update)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rents_delete)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 53, Short.MAX_VALUE)
                        .add(rents_load)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rents_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(rents_scroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        customers_table.setModel(new CustomerTableModel());
        customers_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn customers_col_id = customers_table.getColumnModel().getColumn(0);
        customers_col_id.setMaxWidth(40);
        TableColumn customers_col_firstname = customers_table.getColumnModel().getColumn(1);
        TableColumn customers_col_lastname = customers_table.getColumnModel().getColumn(2);
        TableColumn customers_col_birth = customers_table.getColumnModel().getColumn(3);
        customers_col_birth.setMaxWidth(100);
        TableColumn customers_col_email = customers_table.getColumnModel().getColumn(4);
        customers_scroll.setViewportView(customers_table);

        customers_add.setText(bundle.getString("Autorental.customers_add.text")); // NOI18N
        customers_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customers_addActionPerformed(evt);
            }
        });

        customers_update.setText(bundle.getString("Autorental.customers_update.text")); // NOI18N
        customers_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customers_updateActionPerformed(evt);
            }
        });

        customers_delete.setText(bundle.getString("Autorental.customers_delete.text")); // NOI18N
        customers_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customers_deleteActionPerformed(evt);
            }
        });

        customers_title.setFont(customers_title.getFont().deriveFont(customers_title.getFont().getStyle() | java.awt.Font.BOLD, customers_title.getFont().getSize()+6));
        customers_title.setText(bundle.getString("Autorental.customers_title.text")); // NOI18N

        customers_use.setText(bundle.getString("Autorental.customers_use.text")); // NOI18N
        customers_use.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customers_useActionPerformed(evt);
            }
        });

        customers_load.setText(bundle.getString("Autorental.customers_load.text")); // NOI18N
        customers_load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customers_loadActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout customersLayout = new org.jdesktop.layout.GroupLayout(customers);
        customers.setLayout(customersLayout);
        customersLayout.setHorizontalGroup(
            customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, customersLayout.createSequentialGroup()
                .addContainerGap()
                .add(customers_scroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(customers_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(customers_update, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(customers_delete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(customers_add, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(customers_use, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(customers_load, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(customers_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        customersLayout.setVerticalGroup(
            customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(customersLayout.createSequentialGroup()
                .addContainerGap()
                .add(customersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(customersLayout.createSequentialGroup()
                        .add(customers_title)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customers_add)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customers_update)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customers_delete)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customers_use)
                        .add(18, 18, Short.MAX_VALUE)
                        .add(customers_load)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(customers_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(customers_scroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        cars_table.setModel(new CarTableModel());
        cars_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn cars_col_id = cars_table.getColumnModel().getColumn(0);
        cars_col_id.setMaxWidth(40);
        TableColumn cars_col_model = cars_table.getColumnModel().getColumn(1);
        TableColumn cars_col_plate = cars_table.getColumnModel().getColumn(2);
        cars_col_plate.setMaxWidth(120);
        TableColumn cars_col_fee = cars_table.getColumnModel().getColumn(3);
        cars_col_fee.setMaxWidth(100);
        cars_scroll.setViewportView(cars_table);

        cars_add.setText(bundle.getString("Autorental.cars_add.text")); // NOI18N
        cars_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cars_addActionPerformed(evt);
            }
        });

        cars_update.setText(bundle.getString("Autorental.cars_update.text")); // NOI18N
        cars_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cars_updateActionPerformed(evt);
            }
        });

        cars_delete.setText(bundle.getString("Autorental.cars_delete.text")); // NOI18N
        cars_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cars_deleteActionPerformed(evt);
            }
        });

        cars_title.setFont(cars_title.getFont().deriveFont(cars_title.getFont().getStyle() | java.awt.Font.BOLD, cars_title.getFont().getSize()+6));
        cars_title.setText(bundle.getString("Autorental.cars_title.text")); // NOI18N

        cars_use.setText(bundle.getString("Autorental.cars_use.text")); // NOI18N
        cars_use.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cars_useActionPerformed(evt);
            }
        });

        cars_load.setText(bundle.getString("Autorental.cars_load.text")); // NOI18N
        cars_load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cars_loadActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout carsLayout = new org.jdesktop.layout.GroupLayout(cars);
        cars.setLayout(carsLayout);
        carsLayout.setHorizontalGroup(
            carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, carsLayout.createSequentialGroup()
                .addContainerGap()
                .add(cars_scroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(cars_title, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(cars_update, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(cars_delete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(cars_add, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(cars_use, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(cars_load, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cars_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        carsLayout.setVerticalGroup(
            carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(carsLayout.createSequentialGroup()
                .addContainerGap()
                .add(carsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(carsLayout.createSequentialGroup()
                        .add(cars_title)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cars_add)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cars_update)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cars_delete)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cars_use)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                        .add(cars_load)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cars_progress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(cars_scroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout contentLayout = new org.jdesktop.layout.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contentLayout.createSequentialGroup()
                .addContainerGap()
                .add(contentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cars, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(customers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(rents, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contentLayout.createSequentialGroup()
                .addContainerGap()
                .add(rents, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(customers, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cars, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(content, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dialog_cars_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_cars_cancelActionPerformed
	dialog_cars.setVisible(false);
    }//GEN-LAST:event_dialog_cars_cancelActionPerformed

    private void dialog_cars_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_cars_submitActionPerformed
        Car car = new Car();
        /* Model */
	car.setModel(dialog_cars_modelInput.getText());
        /* Plate */
	car.setPlate(dialog_cars_plateInput.getText());
        /* Fee */
        try {
            car.setFee(BigDecimal.valueOf(Double.parseDouble(dialog_cars_feeInput.getText())).setScale(2));
        } catch(NumberFormatException e) {
            String msg = "Car fee wrong format";
            LOGGER.log(Level.INFO, msg);
        }
        
        try {
            /* Car ID */
            if (dialog_cars_idInput.getText().equals("")) { // Add
                LOGGER.log(Level.INFO, "Adding car");
                carManager.addCar(car);
                carTableModel.addCar(car);
            } else { // Update
                LOGGER.log(Level.INFO, "Updating car");
                Long carId = Long.valueOf(dialog_cars_idInput.getText());
                car.setId(carId);
                Car carCached = carManager.findCarById(carId);
                carManager.updateCar(car);
                carTableModel.removeCar(carCached);
                carTableModel.addCar(car);
            }
            dialog_cars.setVisible(false);
        } catch (Exception ex) {
            String msg = "User request failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_dialog_cars_submitActionPerformed

    private void rents_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rents_addActionPerformed
        dialog_rents_idInput.setText("");
        dialog_rents_customerInput.setText("");
        dialog_rents_customerHelper.setText(java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("Autorental.dialog_rents_customerHelper.text"));
        dialog_rents_carInput.setText("");
        dialog_rents_carHelper.setText(java.util.ResourceBundle.getBundle("cz/muni/fi/pv168/autorental/gui/Bundle").getString("Autorental.dialog_rents_carHelper.text"));
        dialog_rents_fromInput.setText("");
        dialog_rents_toInput.setText("");
        dialog_rents_costInput.setText("");
        dialog_rents.pack();
        dialog_rents.setLocationRelativeTo(null);
        dialog_rents.setVisible(true);
    }//GEN-LAST:event_rents_addActionPerformed

    private void customers_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customers_addActionPerformed
        dialog_customers_idInput.setText("");
        dialog_customers_firstnameInput.setText("");
        dialog_customers_lastnameInput.setText("");
        dialog_customers_birthInput.setText("");
        dialog_customers_emailInput.setText("");
        dialog_customers.pack();
        dialog_customers.setLocationRelativeTo(null);
        dialog_customers.setVisible(true);
    }//GEN-LAST:event_customers_addActionPerformed

    private void cars_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cars_addActionPerformed
        dialog_cars_idInput.setText("");
        dialog_cars_modelInput.setText("");
        dialog_cars_plateInput.setText("");
        dialog_cars_feeInput.setText("");
        dialog_cars.pack();
        dialog_cars.setLocationRelativeTo(null);
        dialog_cars.setVisible(true);
    }//GEN-LAST:event_cars_addActionPerformed

    private void cars_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cars_updateActionPerformed
	action = "update";
        
        Long car_id = null;
        try {
            car_id = (Long) carTableModel.getValueAt(cars_table.getSelectedRow(), 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        
        Car car = carManager.findCarById(car_id);
        
        dialog_cars_idInput.setText(String.valueOf(car.getId()));
        dialog_cars_modelInput.setText(car.getModel());
        dialog_cars_plateInput.setText(car.getPlate());
        dialog_cars_feeInput.setText(String.valueOf(car.getFee()));
        
        dialog_cars.pack();
        dialog_cars.setLocationRelativeTo(null);
        dialog_cars.setVisible(true);
    }//GEN-LAST:event_cars_updateActionPerformed

    private void dialog_cars_idInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_cars_idInputActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_dialog_cars_idInputActionPerformed

    private void dialog_customers_idInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_customers_idInputActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_dialog_customers_idInputActionPerformed

    private void dialog_customers_firstnameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_customers_firstnameInputActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_dialog_customers_firstnameInputActionPerformed

    private void dialog_customers_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_customers_submitActionPerformed
        Customer customer = new Customer();
        /* Firstname */
	customer.setFirstname(dialog_customers_firstnameInput.getText());
        /* Lastname */
	customer.setLastname(dialog_customers_lastnameInput.getText());
        /* E-mail */
	customer.setEmail(dialog_customers_emailInput.getText());
        /* Birth */
        try {
            customer.setBirth(Date.valueOf(dialog_customers_birthInput.getText()));
        } catch(IllegalArgumentException e) {
            String msg = "Customer birth wrong format";
            LOGGER.log(Level.SEVERE, msg);
        }
        try {
            /* Customer ID */
            if (dialog_customers_idInput.getText().equals("")) { // Add
                LOGGER.log(Level.INFO, "Adding customer");
                customerManager.addCustomer(customer);
                customerTableModel.addCustomer(customer);
            } else { // Update
                LOGGER.log(Level.INFO, "Updating customer");
                Long customerId = Long.valueOf(dialog_customers_idInput.getText());
                customer.setId(customerId);
                Customer customerCached = customerManager.findCustomerById(customerId);
                customerManager.updateCustomer(customer);
                customerTableModel.removeCustomer(customerCached);
                customerTableModel.addCustomer(customer);
            }
            dialog_customers.setVisible(false);
        } catch (Exception ex) {
            String msg = "User request failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_dialog_customers_submitActionPerformed

    private void dialog_customers_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_customers_cancelActionPerformed
	dialog_customers.setVisible(false);
    }//GEN-LAST:event_dialog_customers_cancelActionPerformed

    private void dialog_rents_idInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_rents_idInputActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_dialog_rents_idInputActionPerformed

    private void dialog_rents_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_rents_submitActionPerformed
	Rent rent = new Rent();
        rent.setCustomer(customerManager.findCustomerById(Long.parseLong(dialog_rents_customerInput.getText())));
        rent.setCar(carManager.findCarById(Long.parseLong(dialog_rents_carInput.getText())));

        /* From, To */
        try {
            rent.setFrom(Date.valueOf(dialog_rents_fromInput.getText()));
            rent.setTo(Date.valueOf(dialog_rents_toInput.getText()));
        } catch (IllegalArgumentException ex) {
            String msg = "Rent from or to wrong format";
            LOGGER.log(Level.SEVERE, msg);
        }
        /* Cost */
        try {
            rent.setCost(BigDecimal.valueOf(Double.parseDouble(dialog_rents_costInput.getText())).setScale(2));
        } catch (NumberFormatException ex) { // chyba prevodu String -> Double
            String msg = "Rent cost wrong format";
            LOGGER.log(Level.SEVERE, msg);
        }
        
        try {
            /* Rent ID */
            if (dialog_rents_idInput.getText().equals("")) { // Add
                LOGGER.log(Level.INFO, "Adding rent");
                rentManager.addRent(rent);
                rentTableModel.addRent(rent);
            } else { // Update
                LOGGER.log(Level.INFO, "Updating rent");
                Long rentId = Long.valueOf(dialog_rents_idInput.getText());
                rent.setId(rentId);
                Rent rentCached = rentManager.findRentById(rentId);
                rentManager.updateRent(rent);
                rentTableModel.removeRent(rentCached);
                rentTableModel.addRent(rent);
            }
            dialog_rents.setVisible(false);
        } catch (Exception ex) {
            String msg = "User request failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_dialog_rents_submitActionPerformed

    private void dialog_rents_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_rents_cancelActionPerformed
	dialog_rents.setVisible(false);
    }//GEN-LAST:event_dialog_rents_cancelActionPerformed

    private void dialog_rents_calculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_rents_calculateActionPerformed
	Rent rent = new Rent();
        rent.setCar(carManager.findCarById(Long.parseLong(dialog_rents_carInput.getText())));

        /* From, To */
        try {
            rent.setFrom(Date.valueOf(dialog_rents_fromInput.getText()));
            rent.setTo(Date.valueOf(dialog_rents_toInput.getText()));
        } catch (IllegalArgumentException ex) {
            String msg = "Rent from or to wrong format";
            LOGGER.log(Level.SEVERE, msg);
        }
        /* Calculate and set cost input */
        dialog_rents_costInput.setText(rent.calculateCost().toString());
        
    }//GEN-LAST:event_dialog_rents_calculateActionPerformed

    private void dialog_customers_emailInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialog_customers_emailInputActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_dialog_customers_emailInputActionPerformed

    private void rents_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rents_updateActionPerformed
        Long rent_id = (Long) rentTableModel.getValueAt(rents_table.getSelectedRow(), 0);
        Rent rent = rentManager.findRentById(rent_id);
        dialog_rents_idInput.setText(String.valueOf(rent.getId()));
        
        dialog_rents_customerInput.setText(String.valueOf(rent.getCustomer().getId()));
        dialog_rents_customerHelper.setText(String.valueOf(rent.getCustomer().toString()));
        
        dialog_rents_carInput.setText(String.valueOf(rent.getCar().getId()));
        dialog_rents_carHelper.setText(String.valueOf(rent.getCar().toString()));
        
        dialog_rents_fromInput.setText(String.valueOf(rentTableModel.getValueAt(rents_table.getSelectedRow(), 3)));
        dialog_rents_toInput.setText(String.valueOf(rentTableModel.getValueAt(rents_table.getSelectedRow(), 4)));
        dialog_rents_costInput.setText(String.valueOf(rentTableModel.getValueAt(rents_table.getSelectedRow(), 5)));
        
        dialog_rents.pack();
        dialog_rents.setLocationRelativeTo(null);
        dialog_rents.setVisible(true);
    }//GEN-LAST:event_rents_updateActionPerformed

    private void customers_useActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customers_useActionPerformed
        Long id = (Long) customerTableModel.getValueAt(customers_table.getSelectedRow(), 0);
        Customer c = customerManager.findCustomerById(id); // TODO: Use TableModel instead of manager
        dialog_rents_customerInput.setText(String.valueOf(id));
        dialog_rents_customerHelper.setText(String.valueOf(c.toString()));
    }//GEN-LAST:event_customers_useActionPerformed

    private void cars_useActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cars_useActionPerformed
        Long id = (Long) carTableModel.getValueAt(cars_table.getSelectedRow(), 0);
        Car c = carManager.findCarById(id); // TODO: Use TableModel instead of manager
        dialog_rents_carInput.setText(String.valueOf(c.getId()));
        dialog_rents_carHelper.setText(String.valueOf(c.toString()));
    }//GEN-LAST:event_cars_useActionPerformed

    private void rents_loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rents_loadActionPerformed
        if (rentsSwingWorker != null) {
            throw new IllegalStateException("Operation is already in progress");
        }
        rents_load.setEnabled(false);
        rents_progress.setValue(0);
        rentTableModel.clear();
        rentsSwingWorker = new RentsSwingWorker();
        rentsSwingWorker.addPropertyChangeListener(rentsProgressListener);
        rentsSwingWorker.execute();
    }//GEN-LAST:event_rents_loadActionPerformed

    private void customers_loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customers_loadActionPerformed
        if (customersSwingWorker != null) {
            throw new IllegalStateException("Operation is already in progress");
        }
        customers_load.setEnabled(false);
        customers_progress.setValue(0);
        customerTableModel.clear();
        customersSwingWorker = new CustomersSwingWorker();
        customersSwingWorker.addPropertyChangeListener(customersProgressListener);
        customersSwingWorker.execute();
    }//GEN-LAST:event_customers_loadActionPerformed

    private void cars_loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cars_loadActionPerformed
        if (carsSwingWorker != null) {
            throw new IllegalStateException("Operation is already in progress");
        }
        cars_load.setEnabled(false);
        cars_progress.setValue(0);
        carTableModel.clear();
        carsSwingWorker = new CarsSwingWorker();
        carsSwingWorker.addPropertyChangeListener(carsProgressListener);
        carsSwingWorker.execute();
    }//GEN-LAST:event_cars_loadActionPerformed

    private void cars_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cars_deleteActionPerformed
        Long car_id = null;
        try {
            car_id = (Long) carTableModel.getValueAt(cars_table.getSelectedRow(), 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            String msg = "No row selected";
            LOGGER.log(Level.INFO, msg);
        }
        
        Car car = carManager.findCarById(car_id);
        try {
            for (Rent rent : rentManager.findAllCarRents(car)) {
                rentManager.removeRent(rent);
                rentTableModel.removeRent(rent);
	    }
            carManager.removeCar(car);
            carTableModel.removeCar(car);
        } catch (Exception ex) {
            String msg = "Deleting failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_cars_deleteActionPerformed

    private void customers_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customers_updateActionPerformed
        Long customer_id = (Long) customerTableModel.getValueAt(customers_table.getSelectedRow(), 0);
        Customer customer = customerManager.findCustomerById(customer_id);
        dialog_customers_idInput.setText(String.valueOf(customer.getId()));
        
        dialog_customers_firstnameInput.setText(String.valueOf(customer.getFirstname()));
        dialog_customers_lastnameInput.setText(String.valueOf(customer.getLastname()));
        dialog_customers_birthInput.setText(String.valueOf(customer.getBirth()));
        dialog_customers_emailInput.setText(String.valueOf(customer.getEmail()));
        
        dialog_customers.pack();
        dialog_customers.setLocationRelativeTo(null);
        dialog_customers.setVisible(true);
    }//GEN-LAST:event_customers_updateActionPerformed

    private void customers_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customers_deleteActionPerformed
        Long customer_id = null;
        try {
            customer_id = (Long) customerTableModel.getValueAt(customers_table.getSelectedRow(), 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            String msg = "No row selected";
            LOGGER.log(Level.INFO, msg);
        }
        
        Customer customer = customerManager.findCustomerById(customer_id);
        try {
            for (Rent rent : rentManager.findAllCustomerRents(customer)) {
                rentManager.removeRent(rent);
                rentTableModel.removeRent(rent);
	    }
            customerManager.removeCustomer(customer);
            customerTableModel.removeCustomer(customer);
        } catch (Exception ex) {
            String msg = "Deleting failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_customers_deleteActionPerformed

    private void rents_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rents_deleteActionPerformed
        Long rent_id = null;
        try {
            rent_id = (Long) rentTableModel.getValueAt(rents_table.getSelectedRow(), 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            String msg = "No row selected";
            LOGGER.log(Level.INFO, msg);
        }
        
        Rent rent = rentManager.findRentById(rent_id);
        try {
            rentManager.removeRent(rent);
            rentTableModel.removeRent(rent);
        } catch (Exception ex) {
            String msg = "Deleting failed";
            LOGGER.log(Level.INFO, msg);
        }
    }//GEN-LAST:event_rents_deleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
	/*
	 * Set the Nimbus look and feel
	 */
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
	 * If Nimbus (introduced in Java SE 6) is not available, stay with the
	 * default look and feel. For details see
	 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(Autorental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(Autorental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(Autorental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(Autorental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/*
	 * Create and display the form
	 */
	java.awt.EventQueue.invokeLater(new Runnable() {

	    public void run() {
		new Autorental().setVisible(true);
	    }
	});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cars;
    private javax.swing.JButton cars_add;
    private javax.swing.JButton cars_delete;
    private javax.swing.JButton cars_load;
    private javax.swing.JProgressBar cars_progress;
    private javax.swing.JScrollPane cars_scroll;
    private javax.swing.JTable cars_table;
    private javax.swing.JLabel cars_title;
    private javax.swing.JButton cars_update;
    private javax.swing.JButton cars_use;
    private javax.swing.JPanel content;
    private javax.swing.JPanel customers;
    private javax.swing.JButton customers_add;
    private javax.swing.JButton customers_delete;
    private javax.swing.JButton customers_load;
    private javax.swing.JProgressBar customers_progress;
    private javax.swing.JScrollPane customers_scroll;
    private javax.swing.JTable customers_table;
    private javax.swing.JLabel customers_title;
    private javax.swing.JButton customers_update;
    private javax.swing.JButton customers_use;
    private javax.swing.JDialog dialog_cars;
    private javax.swing.JButton dialog_cars_cancel;
    private javax.swing.JTextField dialog_cars_feeInput;
    private javax.swing.JLabel dialog_cars_feeLabel;
    private javax.swing.JTextField dialog_cars_idInput;
    private javax.swing.JLabel dialog_cars_idLabel;
    private javax.swing.JTextField dialog_cars_modelInput;
    private javax.swing.JLabel dialog_cars_modelLabel;
    private javax.swing.JTextField dialog_cars_plateInput;
    private javax.swing.JLabel dialog_cars_plateLabel;
    private javax.swing.JButton dialog_cars_submit;
    private javax.swing.JDialog dialog_customers;
    private javax.swing.JTextField dialog_customers_birthInput;
    private javax.swing.JLabel dialog_customers_birthLabel;
    private javax.swing.JButton dialog_customers_cancel;
    private javax.swing.JTextField dialog_customers_emailInput;
    private javax.swing.JLabel dialog_customers_emailLabel;
    private javax.swing.JTextField dialog_customers_firstnameInput;
    private javax.swing.JLabel dialog_customers_firstnameLabel;
    private javax.swing.JTextField dialog_customers_idInput;
    private javax.swing.JLabel dialog_customers_idLabel;
    private javax.swing.JTextField dialog_customers_lastnameInput;
    private javax.swing.JLabel dialog_customers_lastnameLabel;
    private javax.swing.JButton dialog_customers_submit;
    private javax.swing.JDialog dialog_rents;
    private javax.swing.JButton dialog_rents_calculate;
    private javax.swing.JButton dialog_rents_cancel;
    private javax.swing.JLabel dialog_rents_carHelper;
    private javax.swing.JTextField dialog_rents_carInput;
    private javax.swing.JLabel dialog_rents_carLabel;
    private javax.swing.JTextField dialog_rents_costInput;
    private javax.swing.JLabel dialog_rents_costLabel;
    private javax.swing.JLabel dialog_rents_customerHelper;
    private javax.swing.JTextField dialog_rents_customerInput;
    private javax.swing.JLabel dialog_rents_customerLabel;
    private javax.swing.JTextField dialog_rents_fromInput;
    private javax.swing.JLabel dialog_rents_fromLabel;
    private javax.swing.JTextField dialog_rents_idInput;
    private javax.swing.JLabel dialog_rents_idLabel;
    private javax.swing.JButton dialog_rents_submit;
    private javax.swing.JTextField dialog_rents_toInput;
    private javax.swing.JLabel dialog_rents_toLabel;
    private javax.swing.JPanel header;
    private javax.swing.JLabel header_title;
    private javax.swing.JPanel rents;
    private javax.swing.JButton rents_add;
    private javax.swing.JButton rents_delete;
    private javax.swing.JButton rents_load;
    private javax.swing.JProgressBar rents_progress;
    private javax.swing.JScrollPane rents_scroll;
    private javax.swing.JTable rents_table;
    private javax.swing.JLabel rents_title;
    private javax.swing.JButton rents_update;
    // End of variables declaration//GEN-END:variables
}

