package es.uvigo.esei.hasmment.gui.overallview;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.gui.MainContent;

@SuppressWarnings("serial")
public class SelectMonthPanel extends JPanel{
	MainContent mc;
	JComboBox<String> months = new JComboBox<String>();
	JComboBox<String> days = new JComboBox<String>();
	LinkedHashSet<String> monthsString;
	ArrayList<DateTime> monthsDateTime;
	ArrayList<DBEntity> asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);	
	HashMap<Integer, String> nombreMeses;
	HashMap<String,Integer> claveMeses;
	
	public SelectMonthPanel(MainContent mc) {
		this.mc=mc;
		initNombreMeses();
		initClaveMeses();
		initPanel();
	}
	
	private void initPanel() {
		JLabel message = new JLabel("Mes a mostrar");
		message.setFont(new Font("Arial", 1, 20));
		String item;
		DateTime date;
		setLayout(new FlowLayout());
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
			months.addItem(s);
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
		days.addItem("Todo el mes");
		for(int i=1;i<=31;i++){
			days.addItem(new Integer(i).toString());
		}
		
		add(message);
		add(days);
		add(months);
		JButton send = new JButton("Mostrar");
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(days.getSelectedIndex() == 0) {
					DateTime month = monthsDateTime.get(months.getSelectedIndex());
					mc.updateMainContent(month,false);
				}
				else{
					DateTime month = monthsDateTime.get(months.getSelectedIndex());
					month = month.plusDays(Integer.parseInt((String)days.getSelectedItem())-1);
					mc.updateMainContent(month,true);
				}
			}
		});
		add(send);
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
