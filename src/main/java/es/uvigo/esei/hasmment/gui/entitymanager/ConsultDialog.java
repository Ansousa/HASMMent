package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

/*Clase base donde heredarán todas las ventanas de consultar */
@SuppressWarnings("serial")
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
