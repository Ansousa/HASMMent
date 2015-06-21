package es.uvigo.esei.hasmment.gui.print;

import java.awt.event.ActionEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableDateTime;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public class PrintUserDialog extends PrintDialog{
	public PrintUserDialog(MainFrame owner) {
		super(owner);
		setUsers();
		setTitle("Imprimir horario usuario");
		setLocationRelativeTo(this.owner);
		setVisible(true);
		pack();
	}
	
	private void setUsers() {
		ArrayList<DBEntity> users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		for (DBEntity dbEntity : users) {
			Usuario user = (Usuario) dbEntity;
			persona.addItem(user.getDni() + ", " + user.getNombre());
		}
	}

	private UsuarioBeanList getBeanList() {
		UsuarioBeanList list = new UsuarioBeanList();		
		String dniUsuario = persona.getSelectedItem().toString().trim().split(",")[0];
		ArrayList<DBEntity> asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		
		String mes = month.getSelectedItem().toString().trim().split(",")[0];
		String año = month.getSelectedItem().toString().trim().split(",")[1];
		MutableDateTime  inicioMes = new MutableDateTime();
		inicioMes.setYear(Integer.parseInt(año.trim()));
		inicioMes.setMonthOfYear(claveMeses.get(mes.trim()));
		inicioMes.setDayOfMonth(1);
		inicioMes.setMillisOfDay(0);
		
		MutableDateTime finMes = new MutableDateTime(new DateTime(inicioMes).plusMonths(1));
		
		Interval interval = new Interval(inicioMes, finMes);
		numeroHoras = 0;
		DateFormat dfDia = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			if(dniUsuario.equals(a.getDniUsuario()) && interval.contains(a.getFechaHoraInicioAsistencia().getTime())) {//comprobamos que en la asistencia participa el usuario y que se encuentre en el mes deseado
				DateTime dia = new DateTime(a.getFechaHoraInicioAsistencia().getTime());
				DateTime diaHoraFin = new DateTime(a.getFechaHoraFinAsistencia().getTime());
				numeroHoras += (diaHoraFin.minus(dia.getMillis()).getMillis()) /(1000*60*60);
				Auxiliar aux = HibernateMethods.getAuxiliar(a.getDniAuxiliar());
				list.addBean(dfDia.format(new Date(dia.getMillis())),
					dfHora.format(new Date(dia.getMillis())),
					dfHora.format(new Date(diaHoraFin.getMillis())),
					aux.getNombre() + " " + aux.getApellido1() + " " +aux.getApellido2());
			}
		}
		
		return list;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportButton) {						
			UsuarioBeanList list = getBeanList();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list.getList());
			Map<String, Object> parameters = new HashMap<String,Object>();
			parameters.put("MesReporte", month.getSelectedItem());
			Usuario a = HibernateMethods.getUsuario(persona.getSelectedItem().toString().trim().split(",")[0]);
			parameters.put("Usuario", a.getNombre() + " " + a.getApellido1() + " " + a.getApellido2());
			parameters.put("TotalHoras", new Double(numeroHoras).toString());		
			try {
				file = new File(getClass().getResource("/reports/horarioUsuario.jasper").toURI());
				JasperPrint print = JasperFillManager.fillReport(file.toString(), parameters, beanColDataSource);
				JasperViewer.viewReport(print);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
