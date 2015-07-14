package es.uvigo.esei.hasmment.gui.print;

import java.awt.event.ActionEvent;
import java.io.IOException;
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
public class PrintAuxDialog extends PrintDialog{
	public PrintAuxDialog(MainFrame owner) {
		super(owner);
		setAuxs();
		setTitle("Imprimir horario auxiliar");
		setLocationRelativeTo(this.owner);
		setVisible(true);
		pack();
	}
	
	private void setAuxs() {
		ArrayList<DBEntity> auxs = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		for (DBEntity dbEntity : auxs) {
			Auxiliar aux = (Auxiliar) dbEntity;
			persona.addItem(aux.getDni() + ", " + aux.getNombre());
		}
	}

	private AuxiliarBeanList getBeanList() {
		AuxiliarBeanList list = new AuxiliarBeanList();		
		String dniAuxiliar = persona.getSelectedItem().toString().trim().split(",")[0];
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
			if(dniAuxiliar.equals(a.getDniAuxiliar()) && interval.contains(a.getFechaHoraInicioAsistencia().getTime())) {//comprobamos que en la asistencia participa el usuario y que se encuentre en el mes deseado
				DateTime dia = new DateTime(a.getFechaHoraInicioAsistencia().getTime());
				DateTime diaHoraFin = new DateTime(a.getFechaHoraFinAsistencia().getTime());
				numeroHoras += (diaHoraFin.minus(dia.getMillis()).getMillis()) /(1000*60*60);
				Usuario u = HibernateMethods.getUsuario(a.getDniUsuario());
				list.addBean(dfDia.format(new Date(dia.getMillis())),
					dfHora.format(new Date(dia.getMillis())),
					dfHora.format(new Date(diaHoraFin.getMillis())),
					u.getNombre() + " " + u.getApellido1() + " " +u.getApellido2(),
					a.getActividad());
			}
		}
		
		return list;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportButton) {						
			AuxiliarBeanList list = getBeanList();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list.getList());
			Map<String, Object> parameters = new HashMap<String,Object>();
			parameters.put("MesReporte", month.getSelectedItem());
			Auxiliar a = HibernateMethods.getAuxiliar(persona.getSelectedItem().toString().trim().split(",")[0]);
			parameters.put("Auxiliar", a.getNombre() + " " + a.getApellido1() + " " + a.getApellido2());
			parameters.put("TotalHoras", new Double(numeroHoras).toString());		
			try {
				file = getClass().getResourceAsStream("/reports/horarioAuxiliar.jasper");
				JasperPrint print = JasperFillManager.fillReport(file, parameters, beanColDataSource);
				JasperViewer.viewReport(print);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			finally {
				if (file != null) {
					try {
						file.close();
					} catch (IOException ex1) {
						ex1.printStackTrace();
					}
				}
			}
		}
	}
}
