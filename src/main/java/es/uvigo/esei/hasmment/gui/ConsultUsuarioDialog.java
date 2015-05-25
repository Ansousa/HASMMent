package es.uvigo.esei.hasmment.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.Vector;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.hibernate.mapping.Table;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.*;

public class ConsultUsuarioDialog extends ConsultDialog implements ActionListener{
	private ArrayList<DBEntity> users;
	private JCheckBox direccionCB, horasCB, modalidadCB;
	private UsuarioTableModel tm;
	private String userSelected;
	
	public ConsultUsuarioDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		userSelected = "";
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
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				userSelected = (String)tm.getValueAt(dataTable.getSelectedRow(), 0);
				System.out.println(userSelected);
			}
		});
				
		tablePanel = new JScrollPane(dataTable);
		tablePanel.setPreferredSize(new Dimension(800,200));
		add(tablePanel,BorderLayout.CENTER);
	}
	
	class UsuarioTableModel extends AbstractTableModel{
		Vector<Vector> data;

		  String columnNames[] = { "DNI", "Nombre", "Apellido1", "Apellido2", "Direccion", "Horas", "Modalidad"};
		
		  public UsuarioTableModel() {
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
	
	private void updateTable(){
		
		if(direccionCB.isSelected())

		if(horasCB.isSelected())

		if(modalidadCB.isSelected())
		
		dataTable.repaint();
	}
	
	@Override
	protected void createButtons() {
		createButton = new JButton("Crear");
		deleteButton = new JButton("Borrar");
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(createButton);
		buttonsPanel.add(deleteButton);
		createButton.addActionListener(this);
		add(buttonsPanel,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreateUsuarioDialog(this.owner, this.mc);
		}
		if(e.getSource() == direccionCB || e.getSource() == horasCB || e.getSource() == modalidadCB) {
			updateTable();
		}
	}
}
