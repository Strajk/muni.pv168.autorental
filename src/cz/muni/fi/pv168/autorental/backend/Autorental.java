package cz.muni.fi.pv168.autorental.backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Autorental {
    
    private static final Logger LOGGER = Logger.getLogger(Autorental.class.getName());
    private Context context;
    private DataSource dataSource;
    private Connection connection;
    
    
    public static void main(String[] args) {
        
        Autorental app = new Autorental();
	
	Locale cestina = new Locale("cs", "CZ");
	ResourceBundle csTexty = ResourceBundle.getBundle("Texty",cestina);
        
        try {
            app.setUp();
        } catch (Exception ex) {
	    String msg = "Application setup failed."; // TODO: Loc
            LOGGER.log(Level.SEVERE, msg, ex);
        }
	
        SwingUtilities.invokeLater(new Runnable() {
	    @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle(java.util.ResourceBundle.getBundle("AutorentalFinalBundle").getString("Window_title"));
		frame.pack();
                frame.setVisible(true);
            }
        });
        
    }
    
    private void setUp() throws Exception {
        
        try {
            Class.forName(java.util.ResourceBundle.getBundle("AutorentalBundle").getString("COM.MYSQL.JDBC.DRIVER"));
            Class.forName(java.util.ResourceBundle.getBundle("AutorentalBundle").getString("COM.DERBY.JDBC.DRIVER"));
        } catch (ClassNotFoundException ex) {
            String msg = java.util.ResourceBundle.getBundle("AutorentalBundle").getString("JDBC DRIVERS REGISTRATION PROBLEM");
            LOGGER.log(Level.SEVERE, msg, ex);
            throw new Exception(msg, ex);
        }
        
        try {
            
            // 1. vybrat poskytovatele jmenne sluzby
            // 2. specifikovat promenne prostredi
            // 3. predat konstruktoru InitialContext()
            context    = (Context) new InitialContext().lookup(java.util.ResourceBundle.getBundle("AutorentalBundle").getString("JAVA:COMP/ENV"));
            dataSource = (DataSource) context.lookup(java.util.ResourceBundle.getBundle("AutorentalBundle").getString("JDBC/AUTORENTAL"));
            LOGGER.log(Level.INFO, context.getEnvironment().toString());
        } catch (NamingException ex) {
            String msg = java.util.ResourceBundle.getBundle("AutorentalBundle").getString("DATASOURCE OBTAINING FROM CONTEXT PROBLEM");
            LOGGER.log(Level.SEVERE, msg, ex);
            throw new Exception(msg, ex);
        }
        
        try {
            connection = dataSource.getConnection();
        } catch (SQLException ex) {
            String msg = java.util.ResourceBundle.getBundle("AutorentalBundle").getString("ERROR WHEN CONNECTING TO DB");
            LOGGER.log(Level.SEVERE, msg, ex);
            throw new Exception(msg, ex);
        }
    }

}
