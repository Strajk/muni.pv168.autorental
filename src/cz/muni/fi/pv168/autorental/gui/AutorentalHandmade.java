/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.*;
import org.apache.commons.dbcp.BasicDataSource;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author strajk
 */
public class AutorentalHandmade extends JFrame {

    private BasicDataSource basicDataSource = new BasicDataSource();
    private AppSwingWorker appSwingWorker;

    RentManager         rentManager;
    RentTableModel      rentTableModel;
    CustomerManager     customerManager;
    CustomerTableModel  customerTableModel;
    CarManager          carManager;
    CarTableModel       carTableModel;

    JPanel frame = new JPanel();

    JPanel header = new JPanel();
    JPanel content = new JPanel();
    JPanel footer = new JPanel();
    
    JPanel rents = new JPanel();
    JPanel customers = new JPanel();
    JPanel cars = new JPanel();


    JScrollPane rents_scroll = new JScrollPane();
    JTable      rents_table = new JTable();
    JButton     rents_add = new JButton();
    JButton     rents_delete = new JButton();
    JButton     rents_update = new JButton();

    JScrollPane customers_scroll = new JScrollPane();
    JTable      customers_table = new JTable();
    JButton     customers_add = new JButton();
    JButton     customers_delete = new JButton();
    JButton     customers_update = new JButton();

    JScrollPane cars_scroll = new JScrollPane();
    JTable      cars_table = new JTable();
    JButton     cars_add = new JButton();
    JButton     cars_delete = new JButton();
    JButton     cars_update = new JButton();


    JDialog    rents_dialog            = new JDialog();
    JLabel     rents_dialog_idLabel    = new JLabel();
    JTextField rents_dialog_idValue    = new JTextField();
    JLabel     rents_dialog_modelLabel = new JLabel();
    JTextField rents_dialog_modelValue = new JTextField();
    JLabel     rents_dialog_plateLabel = new JLabel();
    JTextField rents_dialog_plateValue = new JTextField();
    JLabel     rents_dialog_feeLabel   = new JLabel();
    JTextField rents_dialog_feeValue   = new JTextField();
    JButton    rents_dialog_submit     = new JButton();
    JButton    rents_dialog_cancel     = new JButton();


    JDialog    customers_dialog            = new JDialog();
    JLabel     customers_dialog_idLabel    = new JLabel();
    JTextField customers_dialog_idValue    = new JTextField();
    JLabel     customers_dialog_modelLabel = new JLabel();
    JTextField customers_dialog_modelValue = new JTextField();
    JLabel     customers_dialog_plateLabel = new JLabel();
    JTextField customers_dialog_plateValue = new JTextField();
    JLabel     customers_dialog_feeLabel   = new JLabel();
    JTextField customers_dialog_feeValue   = new JTextField();
    JButton    customers_dialog_submit     = new JButton();
    JButton    customers_dialog_cancel     = new JButton();

    JDialog    cars_dialog            = new JDialog();
    JLabel     cars_dialog_idLabel    = new JLabel();
    JTextField cars_dialog_idValue    = new JTextField();
    JLabel     cars_dialog_modelLabel = new JLabel();
    JTextField cars_dialog_modelValue = new JTextField();
    JLabel     cars_dialog_plateLabel = new JLabel();
    JTextField cars_dialog_plateValue = new JTextField();
    JLabel     cars_dialog_feeLabel   = new JLabel();
    JTextField cars_dialog_feeValue   = new JTextField();
    JButton    cars_dialog_submit     = new JButton();
    JButton    cars_dialog_cancel     = new JButton();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutorentalHandmade app = new AutorentalHandmade();
                app.setVisible(true);
            }
        });
    }

    public AutorentalHandmade() {
        Locale locale_cs = new Locale("cs");
        Locale locale_en = new Locale("en");
        Locale locale_sk = new Locale("sk");
        Locale.setDefault(locale_cs);

	    initComponents();

        basicDataSource.setUrl("jdbc:derby://localhost:1527/autorental");
        basicDataSource.setUsername("app");
        basicDataSource.setPassword("app");

        appSwingWorker = new AppSwingWorker();
        appSwingWorker.execute();
    }

    private class AppSwingWorker extends SwingWorker<Void, Object> {
        protected Void doInBackground() throws Exception {
            rentTableModel     = (RentTableModel) rents_table.getModel();
            customerTableModel = (CustomerTableModel) customers_table.getModel();
            carTableModel      = (CarTableModel) cars_table.getModel();

            rentManager     = new RentManagerImpl(basicDataSource);
            customerManager = new CustomerManagerImpl(basicDataSource);
            carManager      = new CarManagerImpl(basicDataSource);

            for (Rent rent : rentManager.findAllRents()) {
                publish(rent);
            }
            for (Customer customer : customerManager.findAllCustomers()) {
                publish(customer);
            }
            for (Car car : carManager.findAllCars()) {
                publish(car);
            }

            return null;
        }

        @Override
        protected void process(List<Object> items) {
            System.out.println("Processuju auto");
            for (Object obj : items) {
                if (obj instanceof Rent) {
                    rentTableModel.addRent((Rent) obj);
                } else if (obj instanceof Customer) {
                    customerTableModel.addCustomer((Customer) obj);
                } else if (obj instanceof Car) {
                    carTableModel.addCar((Car) obj);
                } else {
                    String msg = "Undefined Object to process";
                    throw new IllegalArgumentException(msg);
                }
            }
        }
    }

    private void initComponents() {
        frame.setLayout(new BorderLayout());
        frame.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        init_menu();
        init_header();
        init_content();
        init_footer();

        add(frame);
        pack();

        setTitle("Autorental"); //TODO Locale
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init_menu() { // TODO
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenuApp = new JMenu("File");

        JMenuItem jMenuItemAppQuit = new JMenuItem("Quit");
        jMenuItemAppQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            System.exit(0);
            }
        });
        jMenuApp.add(jMenuItemAppQuit);

        jMenuBar.add(jMenuApp);
        setJMenuBar(jMenuBar);
	
    }
    
    private void init_header() {
        header.setBackground(Color.gray);
        header.setPreferredSize(new Dimension(800, 50));
        frame.add(header, BorderLayout.NORTH);
    }
    
    private void init_content() {
        content.setPreferredSize(new Dimension(800, 600));
        content.setLayout(new BorderLayout());

        init_rents();
        init_customers();
        init_cars();

        frame.add(content, BorderLayout.CENTER);
    }

    private void init_rents() {
        rents.setPreferredSize(new Dimension(700, 250));
        rents.setLayout(new BorderLayout());
        rents.setBackground(Color.red);

        rents_scroll.add(rents_table);
        rents.add(rents_scroll, BorderLayout.CENTER);
        rents.add(rents_add, BorderLayout.EAST);
        rents.add(rents_update, BorderLayout.EAST);
        rents.add(rents_delete, BorderLayout.EAST);

        content.add(rents, BorderLayout.CENTER);
    }

    private void init_customers() {
        customers.setLayout(new BorderLayout());
        customers.setBackground(Color.green);

        customers_add.setText("Add");
        customers_update.setText("Update");
        customers_delete.setText("Delete");

        customers_scroll.add(customers_table);
        customers.add(customers_scroll, BorderLayout.CENTER);
        customers.add(customers_add, BorderLayout.EAST);
        customers.add(customers_update, BorderLayout.EAST);
        customers.add(customers_delete, BorderLayout.EAST);

        frame.add(customers, BorderLayout.CENTER);
    }

    private void init_cars() {
        cars.setLayout(new BorderLayout());
        cars.setBackground(Color.blue);

        cars_scroll.add(cars_table);
        cars.add(cars_scroll, BorderLayout.CENTER);
        cars.add(cars_add, BorderLayout.EAST);
        cars.add(cars_update, BorderLayout.EAST);
        cars.add(cars_delete, BorderLayout.EAST);

        frame.add(cars, BorderLayout.CENTER);
    }

    private void init_footer() {
        JPanel container = new JPanel();

        JTextArea jTextAreaLog = new JTextArea("Log");
        jTextAreaLog.setPreferredSize(new Dimension(200, 50));
        container.add(jTextAreaLog);

        frame.add(container, BorderLayout.SOUTH);
    }

    private void initDialogCar() {
        cars_dialog.setPreferredSize(new Dimension(400, 400));
        cars_dialog_idLabel.setPreferredSize(new Dimension(200, 28));
        cars_dialog_idLabel.setText("id");
        cars_dialog_idValue.setPreferredSize(new Dimension(200, 28));
        cars_dialog_idValue.setEditable(false);
        cars_dialog_modelLabel.setPreferredSize(new Dimension(200, 28));
        cars_dialog_modelLabel.setText("model");
        cars_dialog_modelValue.setPreferredSize(new Dimension(200, 28));
        cars_dialog_plateLabel.setPreferredSize(new Dimension(200, 28));
        cars_dialog_plateLabel.setText("plate");
        cars_dialog_plateValue.setPreferredSize(new Dimension(200, 28));
        cars_dialog_feeLabel.setPreferredSize(new Dimension(200, 28));
        cars_dialog_feeLabel.setText("fee");
        cars_dialog_feeValue.setPreferredSize(new Dimension(200, 28));
        cars_dialog_submit.setPreferredSize(new Dimension(50, 30));
        cars_dialog_submit.setText("Submit");
        cars_dialog_cancel.setPreferredSize(new Dimension(50, 30));
        cars_dialog_cancel.setText("Cancel");

        cars_dialog_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cars_dialog.setVisible(false);
            }
        });

        cars_dialog_submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Car car = new Car();
                if (! (cars_dialog_idValue.getText()).isEmpty()) {
                    car.setId(Long.valueOf(cars_dialog_idValue.getText()));
                };
                car.setModel(cars_dialog_modelValue.getText());
                car.setPlate(cars_dialog_plateValue.getText());
                car.setFee(BigDecimal.valueOf(Double.parseDouble(cars_dialog_feeValue.getText())).setScale(2));
                carManager.addCar(car);
                carTableModel.addCar(car);
                cars_dialog.setVisible(false);
            }
        });

    }

    
   
    
}
