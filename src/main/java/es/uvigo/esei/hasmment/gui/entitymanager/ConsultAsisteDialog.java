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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public class ConsultAsisteDialog extends ConsultDialog implements ActionListener{
	private JCheckBox timeFinAsisteCB, actividadCB;
	
	private String[] columnNames = {"Usuario", "Auxiliar", "Hora Inicio Asistencia", "Hora Fin Asistencia", "Actividad"};
	
	private TableColumn timeFinAsisteTC, actividadTC;
	
	public ConsultAsisteDialog(MainFrame owner, MainContent mc) {
		super(owner, mc);
		indexTableSelected = -1;
		this.entities = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		setAsisteDialog();
	}
	
	private void setAsisteDialog() {
		createCheckBoxes();
		createTable();
		createButtons();
		setTitle("Consultar Asistencias");
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
				entities = HibernateMethods.searchInAsiste(buscarTF.getText());
				tm.updateRows(createRows());
				tm.fireTableDataChanged();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {		
			}
		});
		
		searchPanel.add(buscarL);
		searchPanel.add(buscarTF);
		
		timeFinAsisteCB = new JCheckBox("Fin Asistencia");
		actividadCB = new JCheckBox("Actividad");
		
		timeFinAsisteCB.setSelected(true);
		actividadCB.setSelected(true);
		
		timeFinAsisteCB.addActionListener(this);
		actividadCB.addActionListener(this);
		
		checkPanel = new JPanel(new FlowLayout());
		checkPanel.add(timeFinAsisteCB);
		checkPanel.add(actividadCB);
		
		root.add(searchPanel,BorderLayout.NORTH);
		root.add(checkPanel,BorderLayout.SOUTH);
		add(root,BorderLayout.NORTH);
	}
	
	@SuppressWarnings("rawtypes")
	private Vector<Vector> createRows() {
		Vector<Vector> data = new Vector<Vector>();
		for(DBEntity e:entities){
			Asiste a = (Asiste) e;
			Vector<String> row = new Vector<String>();
			Usuario us = HibernateMethods.getUsuario(a.getDniUsuario().trim());
			Auxiliar au = HibernateMethods.getAuxiliar(a.getDniAuxiliar().trim());
			String aux = us.getDni() + ", " + us.getNombre();  
			row.add(aux);
			aux = au.getDni() + ", " + au.getNombre();
			row.add(aux);
			row.add(a.getFechaHoraInicioAsistencia().toString());
			row.add(a.getFechaHoraFinAsistencia().toString());
			row.add(a.getActividad());
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
		timeFinAsisteTC = cm.getColumn(3);
		actividadTC = cm.getColumn(4);
	}
	
	private void updateColums(){
		cm.removeColumn(timeFinAsisteTC);
		cm.removeColumn(actividadTC);
		if(timeFinAsisteCB.isSelected())
			cm.addColumn(timeFinAsisteTC);
		if(actividadCB.isSelected())
			cm.addColumn(actividadTC);
		dataTable.repaint();
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		createButton.addActionListener(this);
		modifyButton.addActionListener(this);
		deleteButton.addActionListener(this);	
	}

	@Override
	protected void updateTable() {
		entities = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		tm.updateRows(createRows());
		tm.fireTableDataChanged();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			new CreateAsisteDialog(this, this.mc);
		}
		if(e.getSource() == modifyButton && indexTableSelected >= 0) {
			new CreateAsisteDialog(this, this.mc, (Asiste)entities.get(indexTableSelected));
		}
		if(e.getSource() == deleteButton && indexTableSelected >= 0) {
			Asiste as = (Asiste)entities.get(indexTableSelected);
			String me = (String)tm.getValueAt(indexTableSelected, 0);
			new DeleteConfirm(this, as, mc, me);
		}
		if(e.getSource() == timeFinAsisteCB || e.getSource() == actividadCB) {
			updateColums();
		}
	}
}
