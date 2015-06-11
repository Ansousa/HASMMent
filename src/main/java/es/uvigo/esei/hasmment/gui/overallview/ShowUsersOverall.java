package es.uvigo.esei.hasmment.gui.overallview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Permiso;
import es.uvigo.esei.hasmment.entities.Usuario;

@SuppressWarnings("serial")
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
		float h;
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
			userName.setText(u.getNombre() + " " + u.getApellido1() + " " + u.getApellido2());
			userName.setForeground(Color.white);
			userPanel.setBackground(new Color(137, 144, 161));
			h = getHours(u);
			//Si no tiene las horas cubiertas
			if(h<u.getHoras()) {
				if(h==0) { //El usuario no tiene ninguna asistencia asignada NEGRO
					userStatus.setIcon(new ImageIcon(getClass().getResource("userBlack.png")));
					userStatus.setToolTipText(u.getDni() + ". El usuario no tiene ninguna hora asignada (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
				else { //El usuario tiene horas pero no las suficientes NARANJA
					userStatus.setIcon(new ImageIcon(getClass().getResource("userOrange.png")));
					userStatus.setToolTipText(u.getDni() + ". El usuario no tiene cubiertas las horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
			}
			//Si tiene las horas necesarias
			else{
				if(h>u.getHoras()) { //El usuario tiene demasiadas horas ROJO
					userStatus.setIcon(new ImageIcon(getClass().getResource("userRed.png")));
					userStatus.setToolTipText(u.getDni() + ". El usuario se pasa en " + h + " horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
				else { //El usuario tiene las horas necesarias VERDE
					userStatus.setIcon(new ImageIcon(getClass().getResource("userGreen.png")));
					userStatus.setToolTipText(u.getDni() + ". El usuario tiene cubiertas las horas (" + h + "/" + u.getHoras() + ")");
					userPanel.add(userStatus,BorderLayout.CENTER);
					userPanel.add(userName,BorderLayout.SOUTH);
					listUsers.add(userPanel);
				}
			}
		}
	}
	
	//Cuantas horas tiene cubiertas el usuario
	private float getHours(Usuario u) {
		float hours;
		ArrayList<DBEntity> asistUsuario = checkAsistsByUser(u);
		hours = countHoursFromAsists(asistUsuario);
		return hours;
	}
	
	private ArrayList<DBEntity> checkAsistsByUser(Usuario u) {
		ArrayList<DBEntity> toRet = new ArrayList<DBEntity>();
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			if(a.getDniUsuario().equals(u.getDni()) && intervalToShow.contains(a.getFechaHoraInicioAsistencia().getTime())){
				toRet.add(a);
			}
		}
		return toRet;
	}
	
	private float countHoursFromAsists(ArrayList<DBEntity> asists) {
		float h = 0;
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			//Vemos que el dia no este en un permiso los dias de permiso
			if(!isDayPermiso(a)) {
				DateTime inicio = new DateTime(a.getFechaHoraInicioAsistencia());
				DateTime fin = new DateTime(a.getFechaHoraFinAsistencia());
				h += fin.getMinuteOfDay() - inicio.getMinuteOfDay();
			}
		}
		return h/60;
	}
	
	private boolean isDayPermiso(Asiste a) {
		for (DBEntity dbEntity : pers) {
			Permiso p = (Permiso) dbEntity;
			if(p.getDni().equals(a.getDniAuxiliar())){
				Interval i = new Interval(p.getFechaInicioPermiso().getTime(), p.getFechaFinPermiso().getTime());
				if(i.contains(a.getFechaHoraInicioAsistencia().getTime()) || i.contains(a.getFechaHoraFinAsistencia().getTime()))
					return true;
			}
		}
		
		return false;
	}
}
