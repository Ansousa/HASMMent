package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public class ConsultUsuarioDialog extends ConsultDialog implements ActionListener{
	private JCheckBox direccionCB, horasCB, modalidadCB;
	
	private String[] columnNames = { "DNI", "Nombre", "Apellido1", "Apellido2", "Direccion", "Horas", "Modalidad"};
	
	private TableColumn direccionTC, horasTC, modalidadTC;
	
	public ConsultUsuarioDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		indexTableSelected = -1;
		this.entities = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
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
		JPanel root = new JPanel(new BorderLayout());
		JPanel searchPanel = new JPanel(new FlowLayout());
		
		buscarL = new JLabel("Buscar");
		buscarTF = new JTextField("", 20);
		
		buscarTF.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				entities = HibernateMethods.searchInUsuario(buscarTF.getText());
				tm.updateRows(createRows());
				tm.fireTableDataChanged();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
		});
		
		searchPanel.add(buscarL);
		searchPanel.add(buscarTF);
		
		direccionCB = new JCheckBox("Direccion");
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
		
		root.add(searchPanel,BorderLayout.NORTH);
		root.add(checkPanel,BorderLayout.SOUTH);
		add(root,BorderLayout.NORTH);
	}
	
	private Vector<Vector> createRows() {
		Vector<Vector> data = new Vector<Vector>();
		for(DBEntity e:entities){
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
		direccionTC = cm.getColumn(4);
		horasTC = cm.getColumn(5);
		modalidadTC = cm.getColumn(6);
	}
	
	@Override
	protected void createButtons() {
		super.createButtons();
		createButton.addActionListener(this);
		modifyButton.addActionListener(this);
		deleteButton.addActionListener(this);
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreateUsuarioDialog(this, this.mc);
		}
		if(e.getSource() == modifyButton && indexTableSelected >= 0) {
			new CreateUsuarioDialog(this,this.mc,(Usuario)entities.get(indexTableSelected));
		}
		if(e.getSource() == deleteButton && indexTableSelected >= 0) {
			Usuario us = (Usuario)entities.get(indexTableSelected);
			String me = (String)tm.getValueAt(indexTableSelected, 0);
			new DeleteConfirm(this, us, mc, me);
		}
		if(e.getSource() == direccionCB || e.getSource() == horasCB || e.getSource() == modalidadCB) {
			updateColums();
		}
	}

	@Override
	protected void updateTable() {
		entities = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		tm.updateRows(createRows());
		tm.fireTableDataChanged();
	}
}
