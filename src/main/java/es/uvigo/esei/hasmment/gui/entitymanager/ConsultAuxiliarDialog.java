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
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

public class ConsultAuxiliarDialog extends ConsultDialog implements ActionListener{
	private JCheckBox horasCB, fechaInicioContratoCB, fechaFinContratoCB;
	
	private String[] columnNames = { "DNI", "Nombre", "Apellido1", "Apellido2", "Horas", "Fecha Inicio Contrato", "Fecha Fin Contrato"};
	
	private TableColumn horasTC, fechaInicioContratoTC, fechaFinContratoTC;
	
	public ConsultAuxiliarDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		indexTableSelected = -1;
		this.entities = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		setAuxiliarDialog();
	}
	
	private void setAuxiliarDialog() {
		createCheckBoxes();
		createTable();
		createButtons();
		setTitle("Consultar Auxiliar");
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
				entities = HibernateMethods.searchInAuxiliar(buscarTF.getText());
				tm.updateRows(createRows());
				tm.fireTableDataChanged();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
		});
		
		searchPanel.add(buscarL);
		searchPanel.add(buscarTF);
		
		horasCB = new JCheckBox("Horas");
		fechaInicioContratoCB = new JCheckBox("Fecha Inicio Contrato");
		fechaFinContratoCB = new JCheckBox("Fecha Fin Contrato");
		
		horasCB.setSelected(true);
		fechaInicioContratoCB.setSelected(true);
		fechaFinContratoCB.setSelected(true);
		
		horasCB.addActionListener(this);
		fechaInicioContratoCB.addActionListener(this);
		fechaFinContratoCB.addActionListener(this);
		
		checkPanel = new JPanel(new FlowLayout());
		checkPanel.add(horasCB);
		checkPanel.add(fechaInicioContratoCB);
		checkPanel.add(fechaFinContratoCB);
		
		root.add(searchPanel,BorderLayout.NORTH);
		root.add(checkPanel,BorderLayout.SOUTH);
		add(root,BorderLayout.NORTH);
	}
	
	private Vector<Vector> createRows() {
		Vector<Vector> data = new Vector<Vector>();
		for(DBEntity e:entities){
			Auxiliar a = (Auxiliar) e;
			Vector<String> row = new Vector<String>();
			row.add(a.getDni());
			row.add(a.getNombre());
			row.add(a.getApellido1());
			row.add(a.getApellido2());
			row.add(new Integer(a.getHoras()).toString());
			row.add(a.getFechaInicioContrato().toString());
			row.add(a.getFechaFinContrato().toString());
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
		horasTC = cm.getColumn(4);
		fechaInicioContratoTC = cm.getColumn(5);
		fechaFinContratoTC = cm.getColumn(6);
	}
	
	@Override
	protected void createButtons() {
		super.createButtons();
		createButton.addActionListener(this);
		modifyButton.addActionListener(this);
		deleteButton.addActionListener(this);
	}
	
	private void updateColums(){
		cm.removeColumn(horasTC);
		cm.removeColumn(fechaInicioContratoTC);
		cm.removeColumn(fechaFinContratoTC);
		if(horasCB.isSelected())
			cm.addColumn(horasTC);
		if(fechaInicioContratoCB.isSelected())
			cm.addColumn(fechaInicioContratoTC);
		if(fechaFinContratoCB.isSelected())
			cm.addColumn(fechaFinContratoTC);
		dataTable.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreateAuxiliarDialog(this, this.mc);
		}
		if(e.getSource() == modifyButton && indexTableSelected >= 0) {
			new CreateAuxiliarDialog(this, this.mc, (Auxiliar)entities.get(indexTableSelected));
		}
		if(e.getSource() == deleteButton && indexTableSelected >= 0) {
			Auxiliar au = (Auxiliar)entities.get(indexTableSelected);
			String me = (String)tm.getValueAt(indexTableSelected, 0);
			new DeleteConfirm(this, au, mc, me);
		}
		if(e.getSource() == horasCB || e.getSource() == fechaInicioContratoCB || e.getSource() == fechaFinContratoCB ) {
			updateColums();
		}
	}

	@Override
	protected void updateTable() {
		entities = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		tm.updateRows(createRows());
		tm.fireTableDataChanged();
	}
}
