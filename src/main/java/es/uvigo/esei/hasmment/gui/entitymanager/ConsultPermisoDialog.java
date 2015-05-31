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
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

public class ConsultPermisoDialog extends ConsultDialog implements ActionListener{
	private JCheckBox fechaFinPermisoCB,tipoPermisoCB;
	
	private TableColumn fechaFinPermisoTC,tipoPermisoTC;
	
	private String[] columnNames = {"Auxiliar", "Fecha Inicio Permiso", "Fecha Fin Permiso", "Tipo Permiso"};
	
	public ConsultPermisoDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		indexTableSelected = 0;
		this.entities = HibernateMethods.getListEntities(HibernateEntities.PERMISO);
		setPermisoDialog();
	}
	
	private void setPermisoDialog() {
		createCheckBoxes();
		createTable();
		createButtons();
		setTitle("Consultar Permisos");
		setLocationRelativeTo(this.getOwner());
		setVisible(true);
		pack();
	}
	
	@Override
	protected void createCheckBoxes() {
		fechaFinPermisoCB = new JCheckBox("Fecha Fin Permiso");
		tipoPermisoCB = new JCheckBox("Tipo permiso");
		
		fechaFinPermisoCB.setSelected(true);
		tipoPermisoCB.setSelected(true);
		
		fechaFinPermisoCB.addActionListener(this);
		tipoPermisoCB.addActionListener(this);
		
		checkPanel = new JPanel(new FlowLayout());
		checkPanel.add(fechaFinPermisoCB);
		checkPanel.add(tipoPermisoCB);
		
		add(checkPanel,BorderLayout.NORTH);
	}
	
	private Vector<Vector> createRows() {
		Vector<Vector> data = new Vector<Vector>();
		for(DBEntity e:entities){
			Permiso p = (Permiso) e;
			Vector<String> row = new Vector<String>();
			Auxiliar a = HibernateMethods.getAuxiliar(p.getDni().trim());
			String aux = a.getDni() + ", " + a.getNombre();  
			row.add(aux);
			row.add(p.getFechaInicioPermiso().toString());
			row.add(p.getFechaFinPermiso().toString());
			row.add(p.getTipo());
			data.add(row);
		}
		return data;
	}
	
	@Override
	protected void createTable() {		
		tm = new ConsultTableModel(columnNames,createRows());
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
		fechaFinPermisoTC = cm.getColumn(2);
		tipoPermisoTC = cm.getColumn(3);
	}
	
	@Override
	protected void createButtons() {
		super.createButtons();
		createButton.addActionListener(this);
		modifyButton.addActionListener(this);
		deleteButton.addActionListener(this);
	}
	
	private void updateColums(){
		cm.removeColumn(fechaFinPermisoTC);
		cm.removeColumn(tipoPermisoTC);
		if(fechaFinPermisoCB.isSelected())
			cm.addColumn(fechaFinPermisoTC);
		if(tipoPermisoCB.isSelected())
			cm.addColumn(tipoPermisoTC);
		dataTable.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreatePermisoDialog(this, this.mc);
		}
		if(e.getSource() == modifyButton) {
			new CreatePermisoDialog(this, this.mc, (Permiso)entities.get(indexTableSelected));
		}
		if(e.getSource() == deleteButton) {
			Permiso pe = (Permiso)entities.get(indexTableSelected);
			String me = (String)tm.getValueAt(indexTableSelected, 0);
			new DeleteConfirm(this, pe, me);
		}
		if(e.getSource() == fechaFinPermisoCB || e.getSource() == tipoPermisoCB) {
			updateColums();
		}
	}

	@Override
	protected void updateTable() {
		entities = HibernateMethods.getListEntities(HibernateEntities.PERMISO);
		tm.updateRows(createRows());
		tm.fireTableDataChanged();
	}
}
