package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.hibernate.exception.ConstraintViolationException;

import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;

@SuppressWarnings("serial")
public class CreateUsuarioDialog extends JDialog implements ActionListener{	
	JLabel dniL,nombreL,apellido1L,apellido2L,direccionL,horasL,modalidadL;
	JTextField dniTF,nombreTF,apellido1TF,apellido2TF,horasTF;
	
	MainContent mc;
	ConsultDialog owner;
	
	JTextArea direccionTA;
	JComboBox<String> modalidadCB;
	JButton createButton,clearButton;
	
	Boolean modify;
	
	String modalidades[] = {"-", "Dependencia", "Prestación Básica" };
	
	Usuario userModifiy;
	
	public CreateUsuarioDialog(ConsultDialog owner, MainContent mc) {
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.modify = false;
		initCreateDialog();
	}
	
	public CreateUsuarioDialog(ConsultDialog owner, MainContent mc, Usuario u) {
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.userModifiy = u;
		modify = true;
		initCreateDialog();
		setToModify();
	}
	
	private void setToModify() {
		dniTF.setText(userModifiy.getDni());
		dniTF.setEditable(false);
		nombreTF.setText(userModifiy.getNombre());
		apellido1TF.setText(userModifiy.getApellido1());
		apellido2TF.setText(userModifiy.getApellido2());
		direccionTA.setText(userModifiy.getDireccion());
		horasTF.setText(new Integer(userModifiy.getHoras()).toString());
		modalidadCB.setSelectedItem(userModifiy.getModalidad());
		
		createButton.setText("Modificar");
	}
	
	private void initCreateDialog() {
		setLocationRelativeTo(this.getOwner());
		if(modify)
			setTitle("Modificar Usuario");
		else
			setTitle("Crear Usuario");
		//setModal(true);
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
		
		form.add(direccionL);
		form.add(direccionTA);
		
		form.add(horasL);
		form.add(horasTF);
		
		form.setLayout(new GridLayout(8,2,10,10));
		
		modalidadL = new JLabel("Modalidad");
		modalidadCB = new JComboBox<String>(modalidades);
		
		form.add(modalidadL);
		form.add(modalidadCB);
		
		form.add(createButton);
		form.add(clearButton);
		
		return form;
	}
	
	private void createUserAction() throws Exception{
		
		try{
			checkUsuarioForm();
		}
		catch(DNIWrongFormatException ex1) {
			throw ex1;
		}
		catch(Exception ex2) {
			throw ex2;
		}
		 
		Usuario u = new Usuario(
					dniTF.getText(), 
					nombreTF.getText(), 
					apellido1TF.getText(), 
					apellido2TF.getText(), 
					direccionTA.getText(), 
					Integer.parseInt(horasTF.getText()), 
					(String)modalidadCB.getSelectedItem());
		
		if(!modify)
			HibernateMethods.saveEntity(u);
		else
			HibernateMethods.modifyEntity(u);
		this.owner.updateTable();
		this.mc.updateMainContent();
	}
	
	private void checkUsuarioForm() throws Exception{
		if(!CheckForms.checkDNI(dniTF.getText().trim())) {
			throw new DNIWrongFormatException(dniTF.getText());
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
		else if(modalidadCB.getSelectedItem() == "-") {
			throw new Exception("No se ha seleccionado una MODALIDAD");
		}
	}
	
	private void clearAction() {
		dniTF.setText("");
		nombreTF.setText("");
		apellido1TF.setText("");
		apellido2TF.setText("");
		direccionTA.setText("");
		horasTF.setText("");
		modalidadCB.setSelectedIndex(0);
	}

	public void actionPerformed(ActionEvent e) { 
		String msg="";
		if(e.getSource() == createButton) {
			try{
				createUserAction();
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
			if(modify)
				setToModify();
			else
				clearAction();				
		}
	}
}
