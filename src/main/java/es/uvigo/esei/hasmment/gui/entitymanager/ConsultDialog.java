package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

/*Clase base donde heredarán todas las ventanas de consultar */
@SuppressWarnings("serial")
public abstract class ConsultDialog extends JDialog{
	protected MainContent mc;
	protected MainFrame owner;
	
	protected JLabel buscarL;
	protected JTextField buscarTF;
	
	protected JTable dataTable;
	protected JButton createButton, modifyButton, deleteButton;
	protected JPanel checkPanel, buttonsPanel;
	protected JScrollPane tablePanel;
	
	protected TableColumnModel cm;
	protected ConsultTableModel tm;
	protected ArrayList<DBEntity> entities;
	protected int indexTableSelected;
	
	protected ConsultDialog(MainFrame owner,MainContent mc) {
		this.owner = owner;
		this.mc = mc;
		setLayout(new BorderLayout());
	}
	
	protected abstract void createCheckBoxes();
	
	protected abstract void createTable();
	
	protected void createButtons() {
		createButton = new JButton("Crear");
		modifyButton = new JButton("Modificar");
		deleteButton = new JButton("Borrar");
		buttonsPanel = new JPanel(new FlowLayout());
		
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(createButton);
		buttonsPanel.add(modifyButton);
		buttonsPanel.add(deleteButton);
		
		add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	protected abstract void updateTable();	
}
