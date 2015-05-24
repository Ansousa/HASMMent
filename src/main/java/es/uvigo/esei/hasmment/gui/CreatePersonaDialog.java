package es.uvigo.esei.hasmment.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.hibernate.exception.ConstraintViolationException;

import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.Usuario;

public class CreatePersonaDialog extends JDialog implements ActionListener{	
	JLabel dniL,nombreL,apellido1L,apellido2L,direccionL,horasL,modalidadL,fInicioCL,fFinCL;
	JTextField dniTF,nombreTF,apellido1TF,apellido2TF,horasTF;
	
	JSpinner fInicioCS,fFinCS;
	JSpinner.DateEditor dateEditorInicio, dateEditorFin;
	
	MainContent mc;
	
	JTextArea direccionTA;
	JComboBox<String> modalidadCB;
	JButton createButton,clearButton;
	String type;
	
	String modalidades[] = {"-", "Dependencia", "Prestación Básica" };
	
	public CreatePersonaDialog(MainFrame owner, String type, MainContent mc) {
		super(owner);
		this.type = type;
		this.mc = mc;
		initCreateDialog();
	}
	
	private void initCreateDialog() {
		setLocationRelativeTo(this.getOwner());
		setTitle("Crear "+ this.type );
		add(createForm());
		setVisible(true);
		pack();
	}
	
	private JPanel createForm() {
		dniTF = new JTextField("",9);
		
		nombreTF = new JTextField("",20);
		apellido1TF = new JTextField("",20);
		apellido2TF = new JTextField("",20);
		direccionTA = new JTextArea(2,20);
		
		horasTF = new JTextField("",2);
		
		
		dniL = new JLabel("DNI");
		nombreL = new JLabel("Nombre");
		apellido1L = new JLabel("Primer Apellido");
		apellido2L = new JLabel("Segundo Apellido");
		direccionL = new JLabel("Direccion");
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
		
		if(this.type == "Usuario") {
			form.add(direccionL);
			form.add(direccionTA);
		}
		
		form.add(horasL);
		form.add(horasTF);
		
		if(this.type=="Usuario") {
			/* Formulario para Usuario*/
			form.setLayout(new GridLayout(8,2,10,10));
			
			modalidadL = new JLabel("Modalidad");
			modalidadCB = new JComboBox<String>(modalidades);
			
			form.add(modalidadL);
			form.add(modalidadCB);
			
		}
		
		else if(this.type == "Auxiliar"){
			/* Formulario para auxiliar*/
			
			form.setLayout(new GridLayout(8,2,10,10));
			fInicioCL = new JLabel("Fecha Inicio Contrato");
			fFinCL = new JLabel("Fecha Fin Contrato");
			
			fInicioCS = new JSpinner(new SpinnerDateModel());
			dateEditorInicio = new JSpinner.DateEditor(fInicioCS,"dd/MM/yyyy");
			fInicioCS.setEditor(dateEditorInicio);
			fInicioCS.setValue(new Date(Calendar.getInstance().getTimeInMillis()));	
			
			fFinCS = new JSpinner(new SpinnerDateModel());
			dateEditorFin = new JSpinner.DateEditor(fFinCS,"dd/MM/yyyy");
			fFinCS.setEditor(dateEditorFin);
			fFinCS.setValue(new Date(Calendar.getInstance().getTimeInMillis()));
			
			form.add(fInicioCL);
			form.add(fInicioCS);
			
			form.add(fFinCL);
			form.add(fFinCS);
		}
		
		form.add(createButton);
		form.add(clearButton);
		
		return form;
	}
	
	private Boolean checkDNI(String dni){
		Pattern dniPattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
		Matcher m = dniPattern.matcher(dni);		
		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		if(dni.length() != 9) {
			return false;
		}
		
		int nDni = Integer.parseInt(dni.substring(0, 8));
		
		if(!m.matches()) {
			return false;			
		}
		else if(letras.toCharArray()[nDni % 23] != dni.substring(8).toCharArray()[0]) {
			return false;
		}
		else {
			return true;
		}
	}
	
	private void createAuxAction() throws Exception{
		java.util.Date d = (java.util.Date)fInicioCS.getValue();
		java.sql.Date dateInicio = new java.sql.Date(d.getTime());
		
		d = (java.util.Date)fFinCS.getValue();
		java.sql.Date dateFin = new java.sql.Date(d.getTime());
		
		if (dateInicio.after(dateFin))
			throw new Exception("La FECHA FIN CONTRATO no puede ser anterior a FECHA INICIO CONTRATO");
		
		Auxiliar a = new Auxiliar(
				dniTF.getText(), 
				nombreTF.getText(), 
				apellido1TF.getText(), 
				apellido2TF.getText(), 
				Integer.parseInt(horasTF.getText()),
				dateInicio,
				dateFin
				);
		if(!checkDNI(a.getDni())){
			throw new DNIWrongFormatException(a.getDni());
		}
		else if(nombreTF.getText().trim().isEmpty()) {
			throw new Exception("El campo NOMBRE no puede estar VACIO");
		}
		else if(apellido1TF.getText().trim().isEmpty()) {
			throw new Exception("El campo PRIMER APELLIDO no puede estar VACIO");
		}
		else {
			HibernateMethods.saveEntity(a);
			this.mc.repaint();
		}
	}
	
	private void createUserAction() throws Exception{
		
		Usuario u = new Usuario(
					dniTF.getText(), 
					nombreTF.getText(), 
					apellido1TF.getText(), 
					apellido2TF.getText(), 
					direccionTA.getText(), 
					Integer.parseInt(horasTF.getText()), 
					(String)modalidadCB.getSelectedItem());
		if(!checkDNI(u.getDni())) {
			throw new DNIWrongFormatException(u.getDni());
		}
		else if(nombreTF.getText().trim().isEmpty()) {
			throw new Exception("El campo NOMBRE no puede estar VACIO");
		}
		else if(apellido1TF.getText().trim().isEmpty()) {
			throw new Exception("El campo PRIMER APELLIDO no puede estar VACIO");
		}
		else if(direccionTA.getText().trim().isEmpty()){
			throw new Exception("El campo DIRECCION no puede estar vacio");
		}
		else if(u.getModalidad() == "-") {
			throw new Exception("No se ha seleccionado una MODALIDAD");
		}
		else {
			HibernateMethods.saveEntity(u);
			this.mc.repaint();
		}
	}
	
	private void clearAction() {
		dniTF.setText("");
		nombreTF.setText("");
		apellido1TF.setText("");
		apellido2TF.setText("");
		direccionTA.setText("");
		horasTF.setText("");
		if(this.type == "Usuario"){
			modalidadCB.setSelectedIndex(0);
		}else if(this.type == "Auxiliar") {
			fInicioCS.setValue(new Date(Calendar.getInstance().getTimeInMillis()));
			fFinCS.setValue(new Date(Calendar.getInstance().getTimeInMillis()));
		}
	}

	public void actionPerformed(ActionEvent e) { 
		String msg="";
		if(e.getSource() == createButton) {
			try{
				if(this.type == "Usuario") {
					createUserAction();
				}
				else if(this.type == "Auxiliar") {
					createAuxAction();
				}
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
			catch(Exception ex0){
				msg = ex0.getMessage();
			}
			finally {
				new DBMessage(this, msg);
			}
		}
		else if(e.getSource() == clearButton) {
			clearAction();
		}
	}
	
	private class DBMessage extends JDialog implements ActionListener {
		JButton buttonOK;
		public DBMessage(JDialog owner, String msg) {
			super(owner);
			JLabel message = new JLabel("Error - " + msg);
			if(msg=="")
				message.setText("Usuario añadido con exito");
			else
				message.setForeground(Color.red);
			message.setBorder(BorderFactory.createEmptyBorder(20,5,20,5));
			
			buttonOK = new JButton("Ok");
			buttonOK.addActionListener(this);
			
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 3;
			add(message,c);
			
			c.gridx = 2;
			c.gridy = 1;
			c.gridwidth = 1;
			
			add(buttonOK,c);
			setLocationRelativeTo(this);
			pack();
			setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == buttonOK){
				this.dispose();
			}
		}
	}
}
