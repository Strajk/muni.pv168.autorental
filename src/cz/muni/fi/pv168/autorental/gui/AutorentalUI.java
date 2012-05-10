/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author strajk
 */
public class AutorentalUI extends JFrame {

    public AutorentalUI() {
	Locale locale_cs = new Locale("cs");
	Locale locale_en = new Locale("en");
	Locale locale_sk = new Locale("sk");
	initUI();
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		AutorentalUI app = new AutorentalUI();
		app.setVisible(true);
	    }
	});
    }

    private void initUI() {
	JPanel panel = new JPanel(new BorderLayout());
	panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
	
	initUIMenu();
	initUINorth(panel);
	initUICenter(panel);
	initUISouth(panel);
		
	add(panel);
	pack();
	
	setTitle("Autorental"); //TODO Locale
	setSize(800, 800);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initUIMenu() {
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
    
    private void initUINorth(JPanel parent) {
	JPanel container = new JPanel();
	
	container.setBackground(Color.gray);
	container.setPreferredSize(new Dimension(800, 50));
	
	parent.add(container, BorderLayout.NORTH);
    }
    
    private void initUICenter(JPanel parent) {
	JPanel container = new JPanel();
	
	container.setPreferredSize(new Dimension(800, 600));
	
	parent.add(container, BorderLayout.CENTER);
    }
    
    private void initUISouth(JPanel parent) {
	JPanel container = new JPanel();
	
	JTextArea jTextAreaLog = new JTextArea("Log");
	jTextAreaLog.setPreferredSize(new Dimension(200, 50));
	container.add(jTextAreaLog);
	
	parent.add(container, BorderLayout.SOUTH);
    }

    
   
    
}
