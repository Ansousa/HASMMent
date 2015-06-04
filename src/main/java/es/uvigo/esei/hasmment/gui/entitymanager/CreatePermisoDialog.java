package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.hibernate.exception.ConstraintViolationException;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.mysql.fabric.xmlrpc.base.Array;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
import es.uvigo.esei.hasmment.gui.MainContent;

public class CreatePermisoDialog extends JDialog implements ActionListener{
	ConsultDialog owner;
	MainContent mc;
	JPanel form;
	ArrayList<DBEntity> auxs;
	
	UtilDateModel dateModelInicio, dateModelFin;
	JDatePanelImpl datePanelInicio, datePanelFin;
	JDatePickerImpl datePickerfInicio, datePickerfFin;
	
	JLabel auxiliarL, fechaInicioPermisoL, fechaFinPermisoL, tipoPermisoL;
	JComboBox<String> auxiliarCB,tipoPermisoCB;
	
	Permiso perModify;
	Boolean modify;
	JTextField auxiliarTF,fechaInicioTF;
	
	JButton createButton, clearButton;
	String[] tipo = {"-", "Vacaciones", "Baja", "Asuntos propios"};
	
	public CreatePermisoDialog(ConsultDialog owner, MainContent mc) {
		super(owner);
		this.owner = owner;
		this.mc = mc;
		this.modify = false;
		initCreateDialog();
	}
	
	public CreatePermisoDialog(ConsultDialog owner, MainContent mc, Permiso p){
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.perModify = p;
		this.modify = true;
		initCreateDialog();
		setToModify();
	}
	
	private void setToModify() {
		Auxiliar a = HibernateMethods.getAuxiliar(perModify.getDni());
		auxiliarTF.setText(a.getDni() + ", " + a.getNombre());
		
		fechaInicioTF.setText(perModify.getFechaInicioPermiso().toString());
		
		dateModelFin.setValue(perModify.getFechaFinPermiso());
		
		tipoPermisoCB.setSelectedItem(perModify.getTipo());
		
		createButton.setText("Modificar");
	}
	
	private void initCreateDialog() {
		auxs = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		if(modify)
			setTitle("Modificar Auxiliar");
		else
			setTitle("Crear Auxiliar");
		add(createForm());
		setLocationRelativeTo(this.getOwner());
		setVisible(true);
		pack();
	}
	
	private JPanel createForm() {
		form = new JPanel();
		form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		auxiliarL = new JLabel("Auxiliar");
		Vector<String> vec = new Vector<String>();
		for(DBEntity e:auxs) {
			Auxiliar a = (Auxiliar)e;
			vec.addElement(a.getDni() + ", " + a.getNombre());
		}
		auxiliarCB = new JComboBox<String>(vec);
		
		Properties p = new Properties();
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		fechaInicioPermisoL = new JLabel("Fecha Inicio Permiso");
		dateModelInicio = new UtilDateModel();
		datePanelInicio = new JDatePanelImpl(dateModelInicio,p);
		datePickerfInicio = new JDatePickerImpl(datePanelInicio, new DateFormatter());
		
		fechaFinPermisoL = new JLabel("Fecha Fin Permiso");
		dateModelFin = new UtilDateModel();
		datePanelFin = new JDatePanelImpl(dateModelFin, p);
		datePickerfFin = new JDatePickerImpl(datePanelFin, new DateFormatter());
		
		tipoPermisoL = new JLabel("Tipo permiso");
		tipoPermisoCB = new JComboBox<String>(tipo);
		
		createButton = new JButton("Crear");
		clearButton = new JButton("Limpiar");
		
		createButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		form.setLayout(new GridLayout(5,2,10,10));
		
		form.add(auxiliarL);
		if(modify){
			auxiliarTF = new JTextField("");
			auxiliarTF.setEditable(false);
			form.add(auxiliarTF);
		}
		else
			form.add(auxiliarCB);
		
		form.add(fechaInicioPermisoL);
		if(modify){
			fechaInicioTF = new JTextField("");
			fechaInicioTF.setEditable(false);
			form.add(fechaInicioTF);
		}
		else
			form.add(datePickerfInicio);
		
		form.add(fechaFinPermisoL);
		form.add(datePickerfFin);
		
		form.add(tipoPermisoL);
		form.add(tipoPermisoCB);
		
		form.add(createButton);
		form.add(clearButton);
		
		return form;
	}
	
	private void createPermisoAction() throws Exception{
		java.util.Date d = (java.util.Date) dateModelInicio.getValue();
		java.sql.Date dateInicio = new java.sql.Date(d.getTime());
		
		d = (java.util.Date) dateModelFin.getValue();
		java.sql.Date dateFin = new java.sql.Date(d.getTime());
		
		String dniAux = auxiliarCB.getSelectedItem().toString().split(",")[0].trim();
		Permiso p = new Permiso(
					dniAux,
					dateInicio,
					dateFin,
					(String)tipoPermisoCB.getSelectedItem()
				);
		if(tipoPermisoCB.getSelectedIndex() == 0) {
			throw new Exception("No se ha seleccionado un TIPO PERMISO");
		}
		else if(p.getFechaInicioPermiso().after(p.getFechaFinPermiso())){
			throw new Exception("El campo Fecha Inicio Contrato no puede ser menor que Fecha Fin Contrato");
		}
		else {
			if(!modify)
				HibernateMethods.saveEntity(p);
			else
				HibernateMethods.modifyEntity(p);
			this.owner.updateTable();
			this.mc.updateMainContent();
		}
	}
	
	private void clearAction() {
		auxiliarCB.setSelectedIndex(0);
		dateModelInicio.setValue(null);
		dateModelFin.setValue(null);
		tipoPermisoCB.setSelectedIndex(0);	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton ) {
			String msg="";
			try{
				createPermisoAction();
			}
			catch(ConstraintViolationException ex1) {
				msg = "Permiso duplicado";
			}
			catch(NullPointerException ex2){
				msg = "Los campos FECHA no pueden ser VACIOS";
			}
			catch (Exception ex0){
				msg = ex0.getMessage();
			}
			finally{
				new DBMessage(this, msg);
			}
		}
		if(e.getSource() == clearButton ) {
			if(!modify)
				setToModify();
			else
				clearAction();
		}
	}
}
