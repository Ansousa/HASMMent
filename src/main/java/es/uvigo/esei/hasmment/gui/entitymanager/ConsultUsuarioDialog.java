package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

public class ConsultUsuarioDialog extends ConsultDialog implements ActionListener{
	private ArrayList<DBEntity> users;
	private JCheckBox direccionCB, horasCB, modalidadCB;
	private UsuarioTableModel tm;
	private int indexTableSelected;
	private JScrollPane tablePanel;
	
	private TableColumnModel cm;
	private TableColumn direccionTC, horasTC, modalidadTC;
	
	public ConsultUsuarioDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		indexTableSelected = 0;
		this.users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		setUsuarioDialog();
	}
	
	private void setUsuarioDialog() {
		createCheckBoxes();
		createTable();
		createButtons();
		setTitle("Consultar Usuario");
		setLocationRelativeTo(this.owner);
		setVisible(true);
		pack();
	}
	
	@Override
	protected void createCheckBoxes() {
		direccionCB = new JCheckBox("direccionCB");
		horasCB = new JCheckBox("Horas");
		modalidadCB = new JCheckBox("Modalidad");
		
		direccionCB.setSelected(true);
		horasCB.setSelected(true);
		modalidadCB.setSelected(true);
		
		direccionCB.addActionListener(this);
		horasCB.addActionListener(this);
		modalidadCB.addActionListener(this);
		
		checkPanel = new JPanel(new FlowLayout());
		checkPanel.add(direccionCB);
		checkPanel.add(horasCB);
		checkPanel.add(modalidadCB);
		
		add(checkPanel,BorderLayout.NORTH);
	}
	
	@Override
	protected void createTable() {		
		tm = new UsuarioTableModel();
		dataTable = new JTable(tm);
		
		dataTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {};
			
			@Override
			public void mousePressed(MouseEvent e) {};
			
			@Override
			public void mouseExited(MouseEvent e) {};
			
			@Override
			public void mouseEntered(MouseEvent e) {};
			
			@Override
			public void mouseClicked(MouseEvent e) {
				indexTableSelected = dataTable.getSelectedRow();
			}
		});
				
		tablePanel = new JScrollPane(dataTable);
		tablePanel.setPreferredSize(new Dimension(800,200));
		add(tablePanel,BorderLayout.CENTER);
		
		cm = dataTable.getTableHeader().getColumnModel();
		direccionTC = cm.getColumn(4);
		horasTC = cm.getColumn(5);
		modalidadTC = cm.getColumn(6);
	}
	
	class UsuarioTableModel extends AbstractTableModel{
			Vector<Vector> data;

		  String columnNames[] = { "DNI", "Nombre", "Apellido1", "Apellido2", "Direccion", "Horas", "Modalidad"};
		
		  public UsuarioTableModel() {
			  updateRows();
		  }
		  
		  public void updateRows() {
			  data = new Vector<Vector>();
			  for(DBEntity e:users){
				  Usuario u = (Usuario) e;
				  Vector<String> row = new Vector<String>();
				  row.add(u.getDni());
				  row.add(u.getNombre());
				  row.add(u.getApellido1());
				  row.add(u.getApellido2());
				  row.add(u.getDireccion());
				  row.add(new Integer(u.getHoras()).toString());
				  row.add(u.getModalidad());
				  data.add(row);
			  }
		  }
		  
		  public int getColumnCount() {
		    return columnNames.length;
		  }
		
		  public String getColumnName(int column) {
		    return columnNames[column];
		  }
		
		  public int getRowCount() {
		    return data.size();
		  }
		
		  public Object getValueAt(int row, int column) {
		    return data.get(row).get(column);
		  }
		
		  public Class getColumnClass(int column) {
		    return (getValueAt(0, column).getClass());
		  }
		
		  public void setValueAt(Object value, int row, int column) {
			  data.get(row).set(column, value);
		  }
		
		  public boolean isCellEditable(int row, int column) {
			  return (column != 0);
		  }
	}
	
	@Override
	protected void createButtons() {
		createButton = new JButton("Crear");
		deleteButton = new JButton("Borrar");
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(createButton);
		buttonsPanel.add(deleteButton);
		createButton.addActionListener(this);
		deleteButton.addActionListener(this);
		add(buttonsPanel,BorderLayout.SOUTH);
	}
	
	private void updateColums(){
		cm.removeColumn(direccionTC);
		cm.removeColumn(horasTC);
		cm.removeColumn(modalidadTC);
		if(direccionCB.isSelected())
			cm.addColumn(direccionTC);
		if(horasCB.isSelected())
			cm.addColumn(horasTC);
		if(modalidadCB.isSelected())
			cm.addColumn(modalidadTC);
		dataTable.repaint();
	}
	
	public void updateRows() {
		users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		tm.updateRows();
		tm.fireTableDataChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreateUsuarioDialog(this, this.mc);
		}
		if(e.getSource() == deleteButton) {
			Usuario us = (Usuario)users.get(indexTableSelected);
			String me = (String)tm.getValueAt(indexTableSelected, 0);
			new DeleteConfirm(this, us, me);
		}
		if(e.getSource() == direccionCB || e.getSource() == horasCB || e.getSource() == modalidadCB) {
			updateColums();
		}
	}
}
