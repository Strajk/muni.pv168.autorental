/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.autorental.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author strajk
 */
public class CarDialogUI extends JDialog {


    public CarDialogUI() {

        initUI();
    }

    public final void initUI() {

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        add(main);

        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        JLabel hint = new JLabel("JDeveloper Productivity Hints");
        hint.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        topPanel.add(hint);

        ImageIcon icon = new ImageIcon("jdev.png");
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(label, BorderLayout.EAST);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);

        topPanel.add(separator, BorderLayout.SOUTH);

        main.add(topPanel);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JTextPane pane = new JTextPane();

        pane.setContentType("text/html");
        String text = "<p><b>Closing windows using the mouse wheel</b></p>" +
            "<p>Clicking with the mouse wheel on an editor tab closes the window. " +
            "This method works also with dockable windows or Log window tabs.</p>";
        pane.setText(text);
        pane.setEditable(false);
        textPanel.add(pane);

        main.add(textPanel);

        JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

        JCheckBox box = new JCheckBox("Show Tips at startup");
        box.setMnemonic(KeyEvent.VK_S);

        boxPanel.add(box);
        main.add(boxPanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton ntip = new JButton("Next Tip");
        ntip.setMnemonic(KeyEvent.VK_N);
        JButton close = new JButton("Close");
        close.setMnemonic(KeyEvent.VK_C);

        bottom.add(ntip);
        bottom.add(close);
        main.add(bottom);

        bottom.setMaximumSize(new Dimension(450, 0));

        setTitle("Tip of the Day");
        setSize(new Dimension(450, 350));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
	    @Override
            public void run() {
                CarDialogUI carDialogUI = new CarDialogUI();
                carDialogUI.setVisible(true);
            }
        });
    }
}
