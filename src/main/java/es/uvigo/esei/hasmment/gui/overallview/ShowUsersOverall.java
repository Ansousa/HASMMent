package es.uvigo.esei.hasmment.gui.overallview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jfree.date.EasterSundayRule;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
import es.uvigo.esei.hasmment.entities.Usuario;

public class ShowUsersOverall extends JPanel{
	private ArrayList<DBEntity> users, asists, pers;
	DateTime month;
	Interval intervalToShow;
	
	public ShowUsersOverall(DateTime month) {
		this.month = month;
		this.intervalToShow = getInterval(month);
		users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		pers = HibernateMethods.getListEntities(HibernateEntities.PERMISO);
		initPanel();		
	}
	
	private Interval getInterval(DateTime month){
		DateTime start = new DateTime(month);
		DateTime end = new DateTime(month.plusMonths(1).minusDays(1));
		Interval i = new Interval(start, end);
		return i;
	}
	
	private void initPanel() {
		setBorder(new EmptyBorder(10, 20, 10, 20));
		setBackground(new Color(91, 106, 145));
		setLayout(new BorderLayout(0, 10));
		setVisible(true);
		JLabel title = new JLabel("Usuarios");
		title.setForeground(Color.white);
		add(title,BorderLayout.NORTH);
		int h;
		JPanel userPanel, listUsers;
		JLabel userStatus, userName;
		listUsers = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(listUsers,BorderLayout.CENTER);
		for (DBEntity dbEntity : users) {
			userPanel = new JPanel(new BorderLayout(50, 10));
			userPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			userStatus = new JLabel();
			userName = new JLabel();
			Usuario u = (Usuario) dbEntity;
			userName.setText(u.getNombre());
			userName.setForeground(Color.white);
			userPanel.setBackground(new Color(137, 144, 161));
			h = getHours(u);
			//Si no tiene las horas cubiertas
			if(h<u.getHoras()) {
				if(h==0) { //El usuario no tiene ninguna asistencia asignada ROJO
					userStatus.setIcon(new ImageIcon(getClass().getResource("userRed.png")));
					userStatus.setToolTipText("El usuario no tiene ninguna hora asignada (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
				else { //El usuario tiene horas pero no las suficientes NARANJA
					userStatus.setIcon(new ImageIcon(getClass().getResource("userOrange.png")));
					userStatus.setToolTipText("El usuario no tiene cubiertas las horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
			}
			//Si tiene las horas necesarias
			else{
				if(h>u.getHoras()) { //El usuario tiene demasiadas horas MARRON
					userStatus.setIcon(new ImageIcon(getClass().getResource("userBrown.png")));
					userStatus.setToolTipText("El usuario se pasa en " + h + " horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
				else { //El usuario tiene las horas necesarias VERDE
					userStatus.setIcon(new ImageIcon(getClass().getResource("userGreen.png")));
					userStatus.setToolTipText("El usuario tiene cubiertas las horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
			}
		}
	}
	
	//Cuantas horas tiene cubiertas el usuario
	private int getHours(Usuario u) {
		int hours;
		ArrayList<DBEntity> asistUsuario = checkAsistsByUser(u);
		hours = countHoursFromAsists(asistUsuario);
		return hours;
	}
	
	private ArrayList<DBEntity> checkAsistsByUser(Usuario u) {
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			if(a.getDniUsuario().equals(u.getDni()) && intervalToShow.contains(new DateTime(a.getFechaHoraInicioAsistencia()))){
				toRet.add(a);
			}
		}
		return toRet;
	}
	
	private int countHoursFromAsists(ArrayList<DBEntity> asists) {
		int h = 0;
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			long diff = a.getFechaHoraFinAsistencia().getTime() - a.getFechaHoraInicioAsistencia().getTime();
			int numberDays = (int) (diff/(24*60*60*1000));
			//Restamos los dias de permiso
			numberDays -= checkDaysPermiso(a);
			DateTime dtInicio = new DateTime(a.getFechaHoraInicioAsistencia());
			DateTime dtFin = new DateTime(a.getFechaHoraFinAsistencia());
			int minutes = dtFin.getMinuteOfDay() - dtInicio.getMinuteOfDay();
			numberDays++;
			h += minutes * numberDays;
		}
		return h/60;
	}
	
	private int checkDaysPermiso(Asiste a) {
		int days=0;
		for (DBEntity dbEntity : pers) {
			Permiso p = (Permiso) dbEntity;
			if(p.getDni().equals(a.getDniAuxiliar())) {
				DateTime datePermisoInicio = new DateTime(p.getFechaInicioPermiso());
				DateTime datePermisoFin = new DateTime(p.getFechaFinPermiso());
				Interval intervalPermiso = new Interval(datePermisoInicio,datePermisoFin.plusDays(1));
				
				DateTime dateAsistInicio = new DateTime(a.getFechaHoraInicioAsistencia());
				dateAsistInicio = dateAsistInicio.minus(dateAsistInicio.getMinuteOfDay()*60*1000);
				DateTime dateAsistFin = new DateTime(a.getFechaHoraFinAsistencia());
				dateAsistFin = dateAsistFin.minus(dateAsistFin.getMinuteOfDay()*60*1000);
				
				if(intervalPermiso.contains(dateAsistInicio)) { //La fecha de inicio de la asistencia esta dentro del permiso P-A-P
					if(intervalPermiso.contains(dateAsistFin)){ //La fecha de fin de la asistencia esta dentro del permiso  P-A-A-P
						days = (int)(dateAsistFin.minus(dateAsistInicio.getMillis()).getMillis())/(24*60*60*1000) + 1;//Todo el intervalo de la asistencia queda invalido
					}
					else{//La fecha de fin de la asistencia queda fuera del intervalo del permiso P-A-P-A
						days = (int)(dateAsistFin.minus(datePermisoFin.getMillis()).getMillis())/(24*60*60*1000);//Se eliminan los dias entre el inicio de la asistencia y el final del permiso 
					}
				}
				else {
					if(dateAsistInicio.isBefore(datePermisoInicio)){ //La fecha de inicio de la asistencia esta antes del inicio del permiso A-P-P
						if(intervalPermiso.contains(dateAsistFin)){ //La fecha de fin de la asistencia pertenece al intervalo de asistencia A-P-A-P
							days = (int)(dateAsistFin.minus(datePermisoInicio.getMillis()).getMillis())/(24*60*60*1000)+1;//Se eliminan los dias entre el principio del permiso y el final de la asistencia
						}
						else{
							days = (int)(datePermisoFin.minus(datePermisoInicio.getMillis()).getMillis())/(24*60*60*1000)+1;//Sel eliminan los dias que corresponden al permiso
						}
					}
				}
			}
		}
		
		return days;
	}
}
