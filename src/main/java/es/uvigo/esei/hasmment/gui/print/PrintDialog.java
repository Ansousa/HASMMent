package es.uvigo.esei.hasmment.gui.print;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import org.joda.time.DateTime;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.gui.MainFrame;

@SuppressWarnings("serial")
public abstract class PrintDialog extends JDialog implements ActionListener{
	protected JComboBox<String> persona, month;
	protected JButton exportButton,selectButton;
	
	protected double numeroHoras;
	
	protected MainFrame owner; 
	protected LinkedHashSet<String> monthsString;
	protected ArrayList<DateTime> monthsDateTime;
	protected ArrayList<DBEntity> asists;
	
	protected HashMap<Integer, String> nombreMeses;
	protected HashMap<String,Integer> claveMeses;
	
	protected File file;
	
	public PrintDialog(MainFrame owner) {
		this.owner = owner;
		
		initDialog();
	}
	
	private void initDialog() {
		initNombreMeses();
		initClaveMeses();
		
		setLayout(new BorderLayout());
		
		asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		
		String item;
		DateTime date;
		monthsString = new LinkedHashSet<String>();
		monthsDateTime = new ArrayList<DateTime>();
		
		persona = new JComboBox<String>();
		month = new JComboBox<String>();
		exportButton = new JButton("Exportar Horario");
		selectButton = new JButton("Seleccionar archivo");
		
		monthsString = new LinkedHashSet<String>();
		monthsDateTime = new ArrayList<DateTime>();
		for (DBEntity dbEntity : asists) {
			Asiste a = (Asiste) dbEntity;
			date = new DateTime(a.getFechaHoraInicioAsistencia());
			item = nombreMeses.get(date.getMonthOfYear()) + ", " + date.getYear();
			monthsString.add(item);
			date = new DateTime(a.getFechaHoraFinAsistencia());
			item = nombreMeses.get(date.getMonthOfYear()) + ", " + date.getYear();
			monthsString.add(item);
		}
		for (String s : monthsString) { //Se añade al combobox
			month.addItem(s);
			SimpleDateFormat sdf = new SimpleDateFormat("MM:yyyy");
			
			DateTime month = new DateTime(1);
			try {
				month = new DateTime(sdf.parse(claveMeses.get(s.split(",")[0].trim()) + ":" + s.split(",")[1].trim()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally{
				monthsDateTime.add(month);
			}
		}
		
		exportButton.addActionListener(this);
		
		add(persona,BorderLayout.WEST);
		add(month,BorderLayout.EAST);		
		
		add(exportButton,BorderLayout.SOUTH);		
	}
	
	private void initNombreMeses() {
		nombreMeses = new HashMap<Integer, String>();
		nombreMeses.put(1, "Enero");
		nombreMeses.put(2, "Febrero");
		nombreMeses.put(3, "Marzo");
		nombreMeses.put(4, "Abril");
		nombreMeses.put(5, "Mayo");
		nombreMeses.put(6, "Junio");
		nombreMeses.put(7, "Julio");
		nombreMeses.put(8, "Agosto");
		nombreMeses.put(9, "Septiembre");
		nombreMeses.put(10, "Octubre");
		nombreMeses.put(11, "Noviembre");
		nombreMeses.put(12, "Diciembre");
	}
	
	private void initClaveMeses() {
		claveMeses = new HashMap<String, Integer>();
		claveMeses.put("Enero",1);
		claveMeses.put("Febrero",2);
		claveMeses.put("Marzo",3);
		claveMeses.put("Abril",4);
		claveMeses.put("Mayo",5);
		claveMeses.put("Junio",6);
		claveMeses.put("Julio",7);
		claveMeses.put("Agosto",8);
		claveMeses.put("Septiembre",9);
		claveMeses.put("Octubre",10);
		claveMeses.put("Noviembre",11);
		claveMeses.put("Diciembre",12);
	}
}
