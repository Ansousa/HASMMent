package es.uvigo.esei.hasmment.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public abstract class ConsultDialog extends JDialog{
	protected MainContent mc;
	protected MainFrame owner;
	protected JTable dataTable;
	protected JButton createButton,deleteButton;
	protected JPanel checkPanel, buttonsPanel;
	protected JScrollPane tablePanel;
	
	protected ConsultDialog(MainFrame owner,MainContent mc) {
		this.owner = owner;
		this.mc = mc;
		setLayout(new BorderLayout());
	}
	
	protected abstract void createCheckBoxes();
	
	protected abstract void createTable();
	
	protected abstract void createButtons();
	
}
