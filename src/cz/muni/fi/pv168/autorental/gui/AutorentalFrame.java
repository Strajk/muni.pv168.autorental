package cz.muni.fi.pv168.autorental.gui;

import cz.muni.fi.pv168.autorental.backend.Car;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author strajk
 */
public class AutorentalFrame extends javax.swing.JFrame {
   
    public AutorentalFrame() {
	initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog = new javax.swing.JDialog();
        jContentPanel = new javax.swing.JPanel();
        jRentsSectionPanel = new javax.swing.JPanel();
        jRentsTitleLabel = new javax.swing.JLabel();
        jRentsScrollPane = new javax.swing.JScrollPane();
        jRentsTable = new javax.swing.JTable();
        jRentsAddButtonSample = new javax.swing.JButton();
        jCarsSectionPanel = new javax.swing.JPanel();
        jCarsTitleLabel = new javax.swing.JLabel();
        jCarsScrollPane = new javax.swing.JScrollPane();
        jCarsTable = new javax.swing.JTable();
        jCarsAddButtonSample = new javax.swing.JButton();
        jCustomersSectionPanel = new javax.swing.JPanel();
        jCustomersTitleLabel = new javax.swing.JLabel();
        jCustomersScrollPane = new javax.swing.JScrollPane();
        jCustomersTable = new javax.swing.JTable();
        jCustomersAddButtonSample = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuApp = new javax.swing.JMenu();
        jMenuAdd = new javax.swing.JMenu();
        jMenuLanguage = new javax.swing.JMenu();
        jMenuLanguageRadioEnglish = new javax.swing.JRadioButtonMenuItem();
        jMenuLanguageRadioCzech = new javax.swing.JRadioButtonMenuItem();
        jMenuLanguageRadioSlovak = new javax.swing.JRadioButtonMenuItem();

        org.jdesktop.layout.GroupLayout jDialogLayout = new org.jdesktop.layout.GroupLayout(jDialog.getContentPane());
        jDialog.getContentPane().setLayout(jDialogLayout);
        jDialogLayout.setHorizontalGroup(
            jDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialogLayout.setVerticalGroup(
            jDialogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Autorental");
        setResizable(false);
        setSize(new java.awt.Dimension(1024, 1024));

        jContentPanel.setLayout(new java.awt.BorderLayout());

        jRentsSectionPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jRentsTitleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("gui/Bundle"); // NOI18N
        jRentsTitleLabel.setText(bundle.getString("RENTS")); // NOI18N

        jRentsScrollPane.setPreferredSize(new java.awt.Dimension(700, 404));

        jRentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Customer", "Car", "From", "To", "Cost"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jRentsScrollPane.setViewportView(jRentsTable);
        jRentsTable.getColumnModel().getColumn(0).setResizable(false);
        jRentsTable.getColumnModel().getColumn(5).setResizable(false);

        jRentsAddButtonSample.setText("Add sample car");
        jRentsAddButtonSample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRentsAddButtonSampleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jRentsSectionPanelLayout = new org.jdesktop.layout.GroupLayout(jRentsSectionPanel);
        jRentsSectionPanel.setLayout(jRentsSectionPanelLayout);
        jRentsSectionPanelLayout.setHorizontalGroup(
            jRentsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jRentsSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jRentsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRentsTitleLabel)
                    .add(jRentsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 800, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRentsAddButtonSample, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(253, Short.MAX_VALUE))
        );
        jRentsSectionPanelLayout.setVerticalGroup(
            jRentsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jRentsSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jRentsTitleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRentsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRentsSectionPanelLayout.createSequentialGroup()
                        .add(100, 100, 100)
                        .add(jRentsAddButtonSample))
                    .add(jRentsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(234, Short.MAX_VALUE))
        );

        jContentPanel.add(jRentsSectionPanel, java.awt.BorderLayout.CENTER);

        jCarsSectionPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jCarsTitleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jCarsTitleLabel.setText(bundle.getString("RENTS_1")); // NOI18N

        jCarsScrollPane.setPreferredSize(new java.awt.Dimension(700, 404));

        jCarsTable.setModel(new CarTableModel());
        // jCarsTable.setDefaultRenderer(Availability.class, new AvailabilityCellRenderer());
        jCarsScrollPane.setViewportView(jCarsTable);

        jCarsAddButtonSample.setText("Add sample car");
        jCarsAddButtonSample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCarsAddButtonSampleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jCarsSectionPanelLayout = new org.jdesktop.layout.GroupLayout(jCarsSectionPanel);
        jCarsSectionPanel.setLayout(jCarsSectionPanelLayout);
        jCarsSectionPanelLayout.setHorizontalGroup(
            jCarsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCarsSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jCarsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCarsTitleLabel)
                    .add(jCarsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 800, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCarsAddButtonSample)
                .addContainerGap(243, Short.MAX_VALUE))
        );
        jCarsSectionPanelLayout.setVerticalGroup(
            jCarsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCarsSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jCarsTitleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCarsSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCarsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCarsSectionPanelLayout.createSequentialGroup()
                        .add(100, 100, 100)
                        .add(jCarsAddButtonSample)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jContentPanel.add(jCarsSectionPanel, java.awt.BorderLayout.PAGE_START);

        jCustomersSectionPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jCustomersTitleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jCustomersTitleLabel.setText(bundle.getString("RENTS_2")); // NOI18N

        jCustomersScrollPane.setPreferredSize(new java.awt.Dimension(700, 404));

        jCustomersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "First name", "Last name", "Birth", "E-mail"
            }
        ));
        jCustomersScrollPane.setViewportView(jCustomersTable);

        jCustomersAddButtonSample.setText("Add sample car");
        jCustomersAddButtonSample.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCustomersAddButtonSampleActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jCustomersSectionPanelLayout = new org.jdesktop.layout.GroupLayout(jCustomersSectionPanel);
        jCustomersSectionPanel.setLayout(jCustomersSectionPanelLayout);
        jCustomersSectionPanelLayout.setHorizontalGroup(
            jCustomersSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCustomersSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jCustomersSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCustomersTitleLabel)
                    .add(jCustomersScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 800, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCustomersAddButtonSample)
                .addContainerGap(243, Short.MAX_VALUE))
        );
        jCustomersSectionPanelLayout.setVerticalGroup(
            jCustomersSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCustomersSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jCustomersTitleLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCustomersSectionPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCustomersSectionPanelLayout.createSequentialGroup()
                        .add(94, 94, 94)
                        .add(jCustomersAddButtonSample))
                    .add(jCustomersScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jContentPanel.add(jCustomersSectionPanel, java.awt.BorderLayout.PAGE_END);

        jMenuApp.setText("Autorental");
        jMenuBar.add(jMenuApp);

        jMenuAdd.setText("Add");
        jMenuBar.add(jMenuAdd);

        jMenuLanguage.setText("Language");

        jMenuLanguageRadioEnglish.setSelected(true);
        jMenuLanguageRadioEnglish.setText("English");
        jMenuLanguageRadioEnglish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLanguageRadioEnglishActionPerformed(evt);
            }
        });
        jMenuLanguage.add(jMenuLanguageRadioEnglish);

        jMenuLanguageRadioCzech.setSelected(true);
        jMenuLanguageRadioCzech.setText("Czech");
        jMenuLanguage.add(jMenuLanguageRadioCzech);

        jMenuLanguageRadioSlovak.setSelected(true);
        jMenuLanguageRadioSlovak.setText("Slovak");
        jMenuLanguage.add(jMenuLanguageRadioSlovak);

        jMenuBar.add(jMenuLanguage);

        setJMenuBar(jMenuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jContentPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jContentPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCarsAddButtonSampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCarsAddButtonSampleActionPerformed
	CarTableModel model = (CarTableModel) jCarsTable.getModel();
    }//GEN-LAST:event_jCarsAddButtonSampleActionPerformed

    private void jRentsAddButtonSampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRentsAddButtonSampleActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jRentsAddButtonSampleActionPerformed

    private void jCustomersAddButtonSampleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCustomersAddButtonSampleActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jCustomersAddButtonSampleActionPerformed

    private void jMenuLanguageRadioEnglishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLanguageRadioEnglishActionPerformed
	// TODO add your handling code here:
    }//GEN-LAST:event_jMenuLanguageRadioEnglishActionPerformed

    public static void main(String args[]) {
	
	//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
	 * If Nimbus (introduced in Java SE 6) is not available, stay with the
	 * default look and feel. For details see
	 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	 */
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if (java.util.ResourceBundle.getBundle("AutorentalBundle").getString("NIMBUS").equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    java.util.logging.Logger.getLogger(AutorentalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (InstantiationException ex) {
	    java.util.logging.Logger.getLogger(AutorentalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex) {
	    java.util.logging.Logger.getLogger(AutorentalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	} catch (javax.swing.UnsupportedLookAndFeelException ex) {
	    java.util.logging.Logger.getLogger(AutorentalFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
	//</editor-fold>
	java.awt.EventQueue.invokeLater(new Runnable() {

	    public void run() {
		new AutorentalFrame().setVisible(true);
	    }
	    
	});
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jCarsAddButtonSample;
    private javax.swing.JScrollPane jCarsScrollPane;
    private javax.swing.JPanel jCarsSectionPanel;
    private javax.swing.JTable jCarsTable;
    private javax.swing.JLabel jCarsTitleLabel;
    private javax.swing.JPanel jContentPanel;
    private javax.swing.JButton jCustomersAddButtonSample;
    private javax.swing.JScrollPane jCustomersScrollPane;
    private javax.swing.JPanel jCustomersSectionPanel;
    private javax.swing.JTable jCustomersTable;
    private javax.swing.JLabel jCustomersTitleLabel;
    private javax.swing.JDialog jDialog;
    private javax.swing.JMenu jMenuAdd;
    private javax.swing.JMenu jMenuApp;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuLanguage;
    private javax.swing.JRadioButtonMenuItem jMenuLanguageRadioCzech;
    private javax.swing.JRadioButtonMenuItem jMenuLanguageRadioEnglish;
    private javax.swing.JRadioButtonMenuItem jMenuLanguageRadioSlovak;
    private javax.swing.JButton jRentsAddButtonSample;
    private javax.swing.JScrollPane jRentsScrollPane;
    private javax.swing.JPanel jRentsSectionPanel;
    private javax.swing.JTable jRentsTable;
    private javax.swing.JLabel jRentsTitleLabel;
    // End of variables declaration//GEN-END:variables
    
}
