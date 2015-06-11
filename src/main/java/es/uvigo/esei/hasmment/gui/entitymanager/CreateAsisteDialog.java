package es.uvigo.esei.hasmment.gui.entitymanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.joda.time.DateTime;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainContent;

@SuppressWarnings("serial")
public class CreateAsisteDialog extends JDialog implements ActionListener{
	ConsultDialog owner;
	MainContent mc;
	JPanel form;
	ArrayList<DBEntity> users, auxs;
	
	JSpinner timeInicioS, timeFinS;
	JSpinner.DateEditor timeInicioEditor, timeFinEditor;
	
	UtilDateModel dateModelInicio, dateModelFin;
	JDatePanelImpl datePanelInicio, datePanelFin;
	JDatePickerImpl datePickerfInicio, datePickerfFin;
	
	JLabel usuarioL, auxiliarL, timeInicioAsisteL, fechaInicioAsisteL, timeFinAsisteL, fechaFinAsisteL, actividadL;
	JComboBox<String> usuarioCB, auxiliarCB;
	
	JTextField actividadTF;
	
	Asiste asistModify;
	Boolean modify;
	JTextField auxiliarTF,usuarioTF,fechaInicioTF,timeInicioTF;
	
	JButton createButton, clearButton;
	
	public CreateAsisteDialog(ConsultDialog owner, MainContent mc) {
		super(owner);
		this.owner = owner;
		this.mc = mc;
		this.modify = false;
		initCreateDialog();
	}
	
	public CreateAsisteDialog(ConsultDialog owner, MainContent mc, Asiste a){
		super(owner);
		this.mc = mc;
		this.owner = owner;
		this.modify = true;
		this.asistModify = a;
		initCreateDialog();
		setToModify();
	}
	
	private void setToModify() {
		Usuario u = HibernateMethods.getUsuario(asistModify.getDniUsuario());
		Auxiliar a = HibernateMethods.getAuxiliar(asistModify.getDniAuxiliar());
		
		usuarioTF.setText(u.getDni() + ", " + u.getNombre());
		auxiliarTF.setText(a.getDni() + ", " + a.getNombre());
		
		timeInicioS.setValue(asistModify.getFechaHoraInicioAsistencia());
		dateModelInicio.setValue(asistModify.getFechaHoraInicioAsistencia());
		
		timeFinS.setValue(asistModify.getFechaHoraFinAsistencia());
		dateModelFin.setValue(asistModify.getFechaHoraFinAsistencia());
		
		actividadTF.setText(asistModify.getActividad());
		
		createButton.setText("Modificar");
	}
	
	private void initCreateDialog(){
		users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		auxs =  HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		if(modify)
			setTitle("Modificar Asistencia");
		else
			setTitle("Crear Asistencia");
		add(createForm());
		setLocationRelativeTo(this.getOwner());
		setVisible(true);
		pack();
	}
	
	private JPanel createForm() {
		form = new JPanel();
		form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		usuarioL = new JLabel("Usuario");
		Vector<String> vec = new Vector<String>();
		for(DBEntity e:users) {
			Usuario u = (Usuario)e;
			vec.addElement(u.getDni() + ", " + u.getNombre());
		}
		usuarioCB = new JComboBox<String>(vec);
		
		auxiliarL = new JLabel("Auxiliar");
		vec = new Vector<String>();
		for(DBEntity e:auxs) {
			Auxiliar a = (Auxiliar)e;
			vec.addElement(a.getDni() + ", " + a.getNombre());
		}
		auxiliarCB = new JComboBox<String>(vec);
		
		Properties p = new Properties();
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		dateModelInicio = new UtilDateModel();
		datePanelInicio = new JDatePanelImpl(dateModelInicio,p);
		datePickerfInicio = new JDatePickerImpl(datePanelInicio, new DateFormatter());
		
		dateModelFin = new UtilDateModel();
		datePanelFin = new JDatePanelImpl(dateModelFin, p);
		datePickerfFin = new JDatePickerImpl(datePanelFin, new DateFormatter());
		
		timeInicioS = new JSpinner(new SpinnerDateModel());
		timeInicioEditor = new JSpinner.DateEditor(timeInicioS, "HH:mm");
		timeInicioS.setEditor(timeInicioEditor);
		
		timeFinS = new JSpinner(new SpinnerDateModel());
		timeFinEditor = new JSpinner.DateEditor(timeFinS, "HH:mm");
		timeFinS.setEditor(timeFinEditor);
		
		timeInicioAsisteL = new JLabel("Hora Inicio Asistencia");
		fechaInicioAsisteL = new JLabel("Fecha Inicio Asistencia");
		
		timeFinAsisteL = new JLabel("Hora Fin Asistencia");
		fechaFinAsisteL = new JLabel("Fecha Fin Asistencia");
		
		actividadL = new JLabel("Actividad");
		actividadTF = new JTextField("");
		
		createButton = new JButton("Crear");
		clearButton = new JButton("Limpiar");
		
		createButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		form.setLayout(new GridLayout(8, 2));
		
		form.add(usuarioL);
		if(modify){
			usuarioTF = new JTextField("");
			usuarioTF.setEditable(false);
			form.add(usuarioTF);
		}
		else
			form.add(usuarioCB);
		
		form.add(auxiliarL);
		if(modify){
			auxiliarTF = new JTextField("");
			auxiliarTF.setEditable(false);
			form.add(auxiliarTF);
		}
		else
			form.add(auxiliarCB);
		
		form.add(timeInicioAsisteL);
		form.add(timeInicioS);
		
		form.add(fechaInicioAsisteL);
		form.add(datePickerfInicio);
		
		form.add(timeFinAsisteL);
		form.add(timeFinS);
		
		form.add(fechaFinAsisteL);
		form.add(datePickerfFin);
		
		form.add(actividadL);
		form.add(actividadTF);
		
		form.add(createButton);
		form.add(clearButton);
		
		return form;
	}
	
	private void createAsisteAction() throws Exception {
		java.util.Date d = (java.util.Date) dateModelInicio.getValue();
		java.sql.Date dateInicio = new java.sql.Date(d.getTime());
		d = (java.util.Date) dateModelFin.getValue();
		java.sql.Date dateFin = new java.sql.Date(d.getTime());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Date date = dateFormat.parse(dateInicio + " " + timeInicioEditor.getFormat().format(timeInicioS.getValue()));
		Timestamp timeStampInicio = new Timestamp(date.getTime());
		
		date = dateFormat.parse(dateFin + " " + timeFinEditor.getFormat().format(timeFinS.getValue()));
		Timestamp timeStampFin = new Timestamp(date.getTime());
		
		String dniUsuario = usuarioCB.getSelectedItem().toString().split(",")[0].trim();
		String dniAuxiliar = auxiliarCB.getSelectedItem().toString().split(",")[0].trim();
		
		/*Comprobamos que la asistencia creada no empieza / acaba dentro de otra asistencia
		 * 
		 */
		
		boolean errorIsIn = false;
		ArrayList<DBEntity> asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		for (DBEntity dbEntity : asists) {
			Asiste as = (Asiste) dbEntity;//Pasamos a formate DateTime para comparar
			if(as.getDniUsuario().equals(dniUsuario) && as.getDniAuxiliar().equals(dniAuxiliar)){ //Comprobamos si corresponde al mismo usuario y auxiliar
				DateTime dbI = new DateTime(as.getFechaHoraInicioAsistencia()); //Entidad a comparar (db)
				DateTime dbF = new DateTime(as.getFechaHoraFinAsistencia());
				DateTime i = new DateTime(timeStampInicio);//Entidad a introducir
				DateTime f = new DateTime(timeStampFin);
				if(dbI.minus(dbI.getMinuteOfDay()*60*1000).isEqual(dbI.minus(dbI.getMinuteOfDay()*60*1000))){//miramos si la fecha de inicio coincide
					if((dbI.getMinuteOfDay() <= i.getMinuteOfDay()) && (i.getMinuteOfDay() <= dbF.getMinuteOfDay())){//comprobamos si el minuto del dia esta en el ragno de la asistencia
						errorIsIn = true;
						break;
					}
				}
				else if((dbI.minus(dbI.getMinuteOfDay()*60*1000)==(f.minus(f.getMinuteOfDay()*60*1000)))){//miramos si la fecha de fin coincide
					if((dbI.getMinuteOfDay() <= f.getMinuteOfDay()) && (f.getMinuteOfDay()) <= dbF.getMinuteOfDay()){
						errorIsIn = true;
						break;						
					}
				}
			}
		}
		
		/*
		 * 
		 */
		
		Asiste a = new Asiste(
					dniUsuario, 
					dniAuxiliar, 
					timeStampInicio, 
					timeStampFin, 
					actividadTF.getText());
		
		if(timeStampInicio.after(timeStampFin)){
			throw new Exception("No puede indicarse que se EMPIEZA la ASISTENCIA despues de que ACABE");
		}
		else if(errorIsIn){
			throw new Exception("Hay un error en la asistencia, solapa asistencia existente");
		}
		else if(actividadTF.getText().trim().isEmpty()){
			throw new Exception("El campo ACTIVIDAD no puede estar VACIO");
		}
		else{
			if(!modify){
				DateTime d1 = new DateTime(a.getFechaHoraInicioAsistencia());
				DateTime d2 = new DateTime(a.getFechaHoraFinAsistencia());
				
				DateTime dayStart = d1;//Dia para empezar
				DateTime hFin; //Hora para acabar
				long hourFinInMilis = d2.getMinuteOfDay()*60*1000;
				Asiste x;
				
				//Calcuar los dias
				d1 = d1.minus(d1.getMinuteOfDay()*60*1000);
				d2 = d2.minus(d2.getMinuteOfDay()*60*1000);
				int days = (int)(d2.minus(d1.getMillis()).getMillis()/(24*60*60*1000)+1);
				for(int i=0;i<days;i++) {
					x = a;
					x.setFechaHoraInicioAsistencia(new Timestamp(dayStart.getMillis()));//Poner la hora de inicio de asistencia
					hFin = dayStart.minus(dayStart.getMinuteOfDay()*60*1000); //Reiniciar la hora del dia
					hFin = hFin.plus(hourFinInMilis);
					x.setFechaHoraFinAsistencia(new Timestamp(hFin.getMillis()));//Poner la hora de fin de asistencia
					HibernateMethods.saveEntity(x);
					dayStart = dayStart.plusDays(1);//Sumamos un dia
				}
			}
			else
				HibernateMethods.modifyEntity(a);
			this.owner.updateTable();
			this.mc.updateMainContent();
		}
	}
	
	private void clearAction(){
		usuarioCB.setSelectedIndex(0);
		auxiliarCB.setSelectedIndex(0);
		
		dateModelInicio.setValue(null);
		dateModelFin.setValue(null);
		
		actividadTF.setText("");
	}
	
	@Override	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton ) {
			String msg="";
			try{
				createAsisteAction();
				this.dispose();
			}
			catch(ConstraintViolationException ex1) {
				msg = "Asistencia duplicada";
			}
			catch(NullPointerException ex2){
				msg = "Los campos FECHA no pueden ser VACIOS";
			}
			catch (Exception ex0){
				msg=ex0.getMessage();
			}
			finally{
				new DBMessage(this, msg);
			}
		}
		if(e.getSource() == clearButton ) {
			if(modify)
				setToModify();
			else
				clearAction();
		}
	}
		
}
