/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.*;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingWorker;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author strajk
 */
public class GUI extends javax.swing.JFrame {

    BasicDataSource basicDataSource = new BasicDataSource();
    
    RentManager rentManager;
    RentTableModel rentTableModel;
    CustomerManager customerManager;
    CustomerTableModel customerTableModel;
    CarManager carManager;
    CarTableModel carTableModel;
    
    /*
     * Prace se SwingWorkerem
     */
    private LoadDataSwingWorker loadDataSwingWorker;

    private class LoadDataSwingWorker extends SwingWorker<Void, Object> {

	@Override
	protected Void doInBackground() throws Exception {
	    rentTableModel = (RentTableModel) jTableRents.getModel();
	    rentManager = new RentManagerImpl(basicDataSource);
	    customerTableModel = (CustomerTableModel) jTableCustomers.getModel();
	    customerManager = new CustomerManagerImpl(basicDataSource);
	    carTableModel = (CarTableModel) jTableCars.getModel();
	    carManager = new CarManagerImpl(basicDataSource);
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

    /*
     * konec prace se SwingWorkerem
     */
    public GUI() {
	Locale.setDefault(new Locale("cs"));
	initComponents();
	
	basicDataSource.setUrl("jdbc:derby://localhost:1527/autorental");
	basicDataSource.setUsername("app");
	basicDataSource.setPassword("app");

	loadDataSwingWorker = new LoadDataSwingWorker();
	loadDataSwingWorker.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogCar = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jDialogCarInputModel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jDialogCarInputPlate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jDialogCarInputFee = new javax.swing.JTextField();
        jDialogCarSubmit = new javax.swing.JButton();
        jDialogCarCancel = new javax.swing.JButton();
        jPanelNorth = new javax.swing.JPanel();
        jLabelAppTitle = new javax.swing.JLabel();
        jPanelSouth = new javax.swing.JPanel();
        jPanelEast = new javax.swing.JPanel();
        jButtonRentsAdd = new javax.swing.JButton();
        jButtonRentsUpdate = new javax.swing.JButton();
        jButtonRentsDelete = new javax.swing.JButton();
        jButtonCustomersAdd = new javax.swing.JButton();
        jButtonCustomersUpdate = new javax.swing.JButton();
        jButtonCustomersDelete = new javax.swing.JButton();
        jButtonCarsAdd = new javax.swing.JButton();
        jButtonCarsUpdate = new javax.swing.JButton();
        jButtonCarsDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jScrollPaneRentsScroll = new javax.swing.JScrollPane();
        jTableRents = new javax.swing.JTable();
        jScrollPaneCustomersScroll = new javax.swing.JScrollPane();
        jTableCustomers = new javax.swing.JTable();
        jScrollPaneCarsScroll = new javax.swing.JScrollPane();
        jTableCars = new javax.swing.JTable();

        jLabel4.setText("Model");

        jDialogCarInputModel.setPreferredSize(new java.awt.Dimension(200, 28));
        jDialogCarInputModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDialogCarInputModelActionPerformed(evt);
            }
        });

        jLabel5.setText("Plate");

        jDialogCarInputPlate.setPreferredSize(new java.awt.Dimension(200, 28));

        jLabel6.setText("Fee");

        jDialogCarInputFee.setPreferredSize(new java.awt.Dimension(200, 28));

        jDialogCarSubmit.setText("Submit");
        jDialogCarSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDialogCarSubmitActionPerformed(evt);
            }
        });

        jDialogCarCancel.setText("Cancel");
        jDialogCarCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDialogCarCancelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jDialogCarLayout = new org.jdesktop.layout.GroupLayout(jDialogCar.getContentPane());
        jDialogCar.getContentPane().setLayout(jDialogCarLayout);
        jDialogCarLayout.setHorizontalGroup(
            jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogCarLayout.createSequentialGroup()
                .addContainerGap()
                .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jDialogCarLayout.createSequentialGroup()
                        .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jDialogCarInputModel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jDialogCarInputPlate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jDialogCarInputFee, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jDialogCarLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jDialogCarCancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jDialogCarSubmit)))
                .addContainerGap())
        );
        jDialogCarLayout.setVerticalGroup(
            jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogCarLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jDialogCarInputModel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jDialogCarInputPlate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jDialogCarInputFee, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jDialogCarLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jDialogCarSubmit)
                    .add(jDialogCarCancel)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelNorth.setPreferredSize(new java.awt.Dimension(1085, 50));

        jLabelAppTitle.setFont(jLabelAppTitle.getFont().deriveFont(jLabelAppTitle.getFont().getStyle() | java.awt.Font.BOLD, jLabelAppTitle.getFont().getSize()+12));
        jLabelAppTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAppTitle.setText("jAutorentalApp");

        org.jdesktop.layout.GroupLayout jPanelNorthLayout = new org.jdesktop.layout.GroupLayout(jPanelNorth);
        jPanelNorth.setLayout(jPanelNorthLayout);
        jPanelNorthLayout.setHorizontalGroup(
            jPanelNorthLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelNorthLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelAppTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelNorthLayout.setVerticalGroup(
            jPanelNorthLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelNorthLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabelAppTitle)
                .add(39, 39, 39))
        );

        getContentPane().add(jPanelNorth, java.awt.BorderLayout.PAGE_START);

        jPanelSouth.setPreferredSize(new java.awt.Dimension(1085, 50));

        org.jdesktop.layout.GroupLayout jPanelSouthLayout = new org.jdesktop.layout.GroupLayout(jPanelSouth);
        jPanelSouth.setLayout(jPanelSouthLayout);
        jPanelSouthLayout.setHorizontalGroup(
            jPanelSouthLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1085, Short.MAX_VALUE)
        );
        jPanelSouthLayout.setVerticalGroup(
            jPanelSouthLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 50, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelSouth, java.awt.BorderLayout.PAGE_END);

        jButtonRentsAdd.setText("Add");
        jButtonRentsAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRentsAddActionPerformed(evt);
            }
        });

        jButtonRentsUpdate.setText("Update");

        jButtonRentsDelete.setText("Delete");

        jButtonCustomersAdd.setText("Add");
        jButtonCustomersAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomersAddActionPerformed(evt);
            }
        });

        jButtonCustomersUpdate.setText("Update");

        jButtonCustomersDelete.setText("Delete");

        jButtonCarsAdd.setText("Add");
        jButtonCarsAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCarsAddActionPerformed(evt);
            }
        });

        jButtonCarsUpdate.setText("Update");
        jButtonCarsUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCarsUpdateActionPerformed(evt);
            }
        });

        jButtonCarsDelete.setText("Delete");

        jLabel1.setText("Rents");

        jLabel2.setText("Customers");

        jLabel3.setText("Cars");

        org.jdesktop.layout.GroupLayout jPanelEastLayout = new org.jdesktop.layout.GroupLayout(jPanelEast);
        jPanelEast.setLayout(jPanelEastLayout);
        jPanelEastLayout.setHorizontalGroup(
            jPanelEastLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelEastLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelEastLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonRentsAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonRentsUpdate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonRentsDelete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCustomersAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCustomersUpdate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCustomersDelete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCarsAdd, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCarsUpdate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButtonCarsDelete, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanelEastLayout.createSequentialGroup()
                        .add(jPanelEastLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelEastLayout.setVerticalGroup(
            jPanelEastLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelEastLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(24, 24, 24)
                .add(jButtonRentsAdd)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonRentsUpdate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonRentsDelete)
                .add(80, 80, 80)
                .add(jLabel2)
                .add(18, 18, 18)
                .add(jButtonCustomersAdd)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCustomersUpdate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCustomersDelete)
                .add(79, 79, 79)
                .add(jLabel3)
                .add(18, 18, 18)
                .add(jButtonCarsAdd)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCarsUpdate)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCarsDelete)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        getContentPane().add(jPanelEast, java.awt.BorderLayout.LINE_END);

        jTableRents.setModel(new RentTableModel());
        jScrollPaneRentsScroll.setViewportView(jTableRents);

        jTableCustomers.setModel(new CustomerTableModel());
        jScrollPaneCustomersScroll.setViewportView(jTableCustomers);

        jTableCars.setModel(new cz.muni.fi.pv168.autorental.gui.CarTableModel());
        jScrollPaneCarsScroll.setViewportView(jTableCars);

        org.jdesktop.layout.GroupLayout jPanelCenterLayout = new org.jdesktop.layout.GroupLayout(jPanelCenter);
        jPanelCenter.setLayout(jPanelCenterLayout);
        jPanelCenterLayout.setHorizontalGroup(
            jPanelCenterLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCenterLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanelCenterLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPaneRentsScroll)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneCustomersScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPaneCarsScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelCenterLayout.setVerticalGroup(
            jPanelCenterLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCenterLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPaneRentsScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPaneCustomersScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPaneCarsScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRentsAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRentsAddActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jButtonRentsAddActionPerformed

    private void jButtonCustomersAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomersAddActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jButtonCustomersAddActionPerformed

    private void jButtonCarsAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCarsAddActionPerformed
	jDialogCar.setVisible(true);
	jDialogCar.setSize(400, 200);
    }//GEN-LAST:event_jButtonCarsAddActionPerformed

    private void jDialogCarInputModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDialogCarInputModelActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jDialogCarInputModelActionPerformed

    private void jDialogCarCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDialogCarCancelActionPerformed
	jDialogCar.setVisible(false);
    }//GEN-LAST:event_jDialogCarCancelActionPerformed

    private void jDialogCarSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDialogCarSubmitActionPerformed
	Car car = new Car();
	car.setModel(jDialogCarInputModel.getText());
	car.setPlate(jDialogCarInputPlate.getText());
	car.setFee(BigDecimal.valueOf(Double.parseDouble(jDialogCarInputFee.getText())).setScale(2));
	carManager.addCar(car);
	carTableModel.addCar(car);
	jDialogCarInputModel.setText("");
	jDialogCarInputPlate.setText("");
	jDialogCarInputFee.setText("");
    }//GEN-LAST:event_jDialogCarSubmitActionPerformed

    private void jButtonCarsUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCarsUpdateActionPerformed
	
	jDialogCar.setVisible(true);
	
	jDialogCarInputModel.setText(String.valueOf(carTableModel.getValueAt(jTableCars.getSelectedRow(), 1)));
	jDialogCarInputPlate.setText(String.valueOf(carTableModel.getValueAt(jTableCars.getSelectedRow(), 2)));
	jDialogCarInputFee.setText(String.valueOf(carTableModel.getValueAt(jTableCars.getSelectedRow(), 3)));
	jDialogCar.setSize(400, 200);
    }//GEN-LAST:event_jButtonCarsUpdateActionPerformed

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
	    java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>

	/*
	 * Create and display the form
	 */
	java.awt.EventQueue.invokeLater(new Runnable() {

	    public void run() {
		new GUI().setVisible(true);
	    }
	});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCarsAdd;
    private javax.swing.JButton jButtonCarsDelete;
    private javax.swing.JButton jButtonCarsUpdate;
    private javax.swing.JButton jButtonCustomersAdd;
    private javax.swing.JButton jButtonCustomersDelete;
    private javax.swing.JButton jButtonCustomersUpdate;
    private javax.swing.JButton jButtonRentsAdd;
    private javax.swing.JButton jButtonRentsDelete;
    private javax.swing.JButton jButtonRentsUpdate;
    private javax.swing.JDialog jDialogCar;
    private javax.swing.JButton jDialogCarCancel;
    private javax.swing.JTextField jDialogCarInputFee;
    private javax.swing.JTextField jDialogCarInputModel;
    private javax.swing.JTextField jDialogCarInputPlate;
    private javax.swing.JButton jDialogCarSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelAppTitle;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelEast;
    private javax.swing.JPanel jPanelNorth;
    private javax.swing.JPanel jPanelSouth;
    private javax.swing.JScrollPane jScrollPaneCarsScroll;
    private javax.swing.JScrollPane jScrollPaneCustomersScroll;
    private javax.swing.JScrollPane jScrollPaneRentsScroll;
    private javax.swing.JTable jTableCars;
    private javax.swing.JTable jTableCustomers;
    private javax.swing.JTable jTableRents;
    // End of variables declaration//GEN-END:variables
}
