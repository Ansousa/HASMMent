package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.exception.ConstraintViolationException;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.gui.MainContent;

@SuppressWarnings("serial")
public class CreateAuxiliarDialog extends JDialog implements ActionListener{	
	JLabel dniL,nombreL,apellido1L,apellido2L,horasL,fInicioCL,fFinCL;
	JTextField dniTF,nombreTF,apellido1TF,apellido2TF,horasTF;
	
	UtilDateModel dateModelInicio, dateModelFin;
	JDatePanelImpl datePanelInicio, datePanelFin;
	JDatePickerImpl datePickerfInicio, datePickerfFin;
	
	MainContent mc;
	ConsultDialog owner;
	
	Auxiliar auxModify;
	Boolean modify;
	
	JButton createButton,clearButton;
	
	public CreateAuxiliarDialog(ConsultDialog owner, MainContent mc) {
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.modify = false;
		initCreateDialog();
	}
	
	public CreateAuxiliarDialog(ConsultDialog owner, MainContent mc, Auxiliar a){
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.auxModify = a;
		this.modify = true;
		initCreateDialog();
		setToModify();
	}
	
	private void setToModify(){
		dniTF.setText(auxModify.getDni());
		dniTF.setEditable(false);
		nombreTF.setText(auxModify.getNombre());
		apellido1TF.setText(auxModify.getApellido1());
		apellido2TF.setText(auxModify.getApellido2());
		horasTF.setText(new Integer(auxModify.getHoras()).toString());
		dateModelInicio.setValue(auxModify.getFechaInicioContrato());
		dateModelFin.setValue(auxModify.getFechaFinContrato());
		createButton.setText("Modificar");
	}
	
	private void initCreateDialog() {
		setLocationRelativeTo(this.getOwner());
		if(modify)
			setTitle("Modificar Auxiliar");
		else
			setTitle("Crear Auxiliar");
		add(createForm());
		setVisible(true);
		pack();
	}
	
	private JPanel createForm() {
		dniTF = new JTextField("",9);
		
		nombreTF = new JTextField("",20);
		apellido1TF = new JTextField("",20);
		apellido2TF = new JTextField("",20);		
		horasTF = new JTextField("",2);	
		
		dniL = new JLabel("DNI");
		nombreL = new JLabel("Nombre");
		apellido1L = new JLabel("Primer Apellido");
		apellido2L = new JLabel("Segundo Apellido");
		horasL = new JLabel("Horas");
		
		createButton = new JButton("Crear");
		clearButton = new JButton("Limpiar");
		
		createButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		JPanel form = new JPanel();
		form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		form.add(dniL);
		form.add(dniTF);
		
		form.add(nombreL);
		form.add(nombreTF);
		
		form.add(apellido1L);
		form.add(apellido1TF);
		
		form.add(apellido2L);
		form.add(apellido2TF);
		
		form.add(horasL);
		form.add(horasTF);
			
		form.setLayout(new GridLayout(8,2,10,10));
		fInicioCL = new JLabel("Fecha Inicio Contrato");
		fFinCL = new JLabel("Fecha Fin Contrato");
		
		Properties p = new Properties();
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		dateModelInicio = new UtilDateModel();
		dateModelFin = new UtilDateModel();
		
		datePanelInicio = new JDatePanelImpl(dateModelInicio,p);
		datePanelFin = new JDatePanelImpl(dateModelFin,p);
		
		datePickerfInicio = new JDatePickerImpl(datePanelInicio, new DateFormatter());
		datePickerfFin = new JDatePickerImpl(datePanelFin, new DateFormatter());
		
		form.add(fInicioCL);
		form.add(datePickerfInicio);
		
		form.add(fFinCL);
		form.add(datePickerfFin);
		
		form.add(createButton);
		form.add(clearButton);
		
		return form;
	}
	
	private void createAuxAction() throws Exception{
		java.util.Date d = (java.util.Date) dateModelInicio.getValue();
		java.sql.Date dateInicio = new java.sql.Date(d.getTime());
		
		d = (java.util.Date) dateModelFin.getValue();
		java.sql.Date dateFin = new java.sql.Date(d.getTime());
		
		Auxiliar a = new Auxiliar(
				dniTF.getText(), 
				nombreTF.getText(), 
				apellido1TF.getText(), 
				apellido2TF.getText(), 
				Integer.parseInt(horasTF.getText()),
				dateInicio,
				dateFin
				);
		if(!CheckForms.checkDNI(a.getDni())){
			throw new DNIWrongFormatException(a.getDni());
		}
		else if(nombreTF.getText().trim().isEmpty()) {
			throw new Exception("El campo NOMBRE no puede estar VACIO");
		}
		else if(apellido1TF.getText().trim().isEmpty()) {
			throw new Exception("El campo PRIMER APELLIDO no puede estar VACIO");
		}
		else if(a.getFechaInicioContrato().after(a.getFechaFinContrato())){
			throw new Exception("El campo Fecha Inicio Contrato no puede ser menor que Fecha Fin Contrato");			
		}
		else {
			if(!modify)
				HibernateMethods.saveEntity(a);
			else
				HibernateMethods.modifyEntity(a);
			this.owner.updateTable();
			this.mc.updateMainContent();
		}
	}
	
	private void clearAction() {
		dniTF.setText("");
		nombreTF.setText("");
		apellido1TF.setText("");
		apellido2TF.setText("");
		horasTF.setText("");
		dateModelInicio.setValue(null);
		dateModelFin.setValue(null);
	}

	public void actionPerformed(ActionEvent e) { 
		String msg="";
		if(e.getSource() == createButton) {
			try{
				createAuxAction();
			}
			catch (ConstraintViolationException ex2) {
				msg =  "DNI duplicado";
			}
			catch(DNIWrongFormatException ex3) {
				msg = "Se ha introducido: " + ex3.getDni() + ", " + ex3.getCorrectDnI();
			}
			catch(NumberFormatException ex) {
				msg = "Formato de numero de horas incorrecto";
			}
			catch(NullPointerException ex4) {
				msg = "Los campos de FECHA no pueden ser VACIOS";
			}
			catch(Exception ex0){
				msg = ex0.getMessage();
			}
			finally {
				new DBMessage(this, msg);
			}
		}
		else if(e.getSource() == clearButton) {
			if(modify)
				setToModify();
			else
				clearAction();
		}
	}
}
