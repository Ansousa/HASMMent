package es.uvigo.esei.hasmment.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.gui.overallview.SelectMonthPanel;
import es.uvigo.esei.hasmment.gui.overallview.ShowAuxsOverall;
import es.uvigo.esei.hasmment.gui.overallview.ShowUsersOverall;

@SuppressWarnings("serial")
public class MainContent extends JPanel{
	MainFrame owner;
	HashMap<Integer, String> nombreMeses;
	
	public MainContent(MainFrame owner) {
		this.owner = owner;
		initNombreMeses();
		initMainContent();
	}
	
	private void initMainContent() {
		setBackground(new Color(18, 42, 101));
		setBorder(new EmptyBorder(50, 50, 50, 50));
		setLayout(new BorderLayout(5, 50));
		DateTime lastMonth = new DateTime(HibernateMethods.getLastDateAsist().getYear(), HibernateMethods.getLastDateAsist().getMonthOfYear(), 1, 0, 0);
		JLabel showMonth = new JLabel();
		JPanel head = new JPanel(new BorderLayout());
		JPanel show = new JPanel(new FlowLayout());
		if(lastMonth.getMillis() == -3600000){
			showMonth = new JLabel("Sin asistencias");
		}
		else
			showMonth = new JLabel(nombreMeses.get(lastMonth.getMonthOfYear()) + " "  + lastMonth.getYear());
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		head.add(new ShowUsersOverall(lastMonth),BorderLayout.SOUTH);
		add(head,BorderLayout.NORTH);
		
		add(new ShowAuxsOverall(lastMonth,false),BorderLayout.CENTER);
		add(new SelectMonthPanel(this),BorderLayout.SOUTH);
		this.owner.pack();
	}
	
	public void updateMainContent() {
		this.removeAll();
		DateTime lastMonth = new DateTime(HibernateMethods.getLastDateAsist().getYear(), HibernateMethods.getLastDateAsist().getMonthOfYear(), 1, 0, 0);
		JLabel showMonth = new JLabel();
		JPanel head = new JPanel(new BorderLayout());
		JPanel show = new JPanel(new FlowLayout());
		if(lastMonth.getMillis() == -3600000){
			showMonth = new JLabel("Sin asistencias");
		}
		else
			showMonth = new JLabel(nombreMeses.get(lastMonth.getMonthOfYear()) + " "  + lastMonth.getYear());
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		head.add(new ShowUsersOverall(lastMonth),BorderLayout.SOUTH);
		add(head,BorderLayout.NORTH);
		
		this.add(new ShowAuxsOverall(lastMonth,false),BorderLayout.CENTER);
		this.add(new SelectMonthPanel(this),BorderLayout.SOUTH);
		this.validate();
		this.repaint();
		this.owner.pack();
	}
	
	public void updateMainContent(DateTime month, boolean selectedDay) {
		this.removeAll();
		JPanel head = new JPanel(new BorderLayout());
		JPanel show = new JPanel(new FlowLayout());
		JLabel showMonth;
		if(!selectedDay)
			showMonth = new JLabel(nombreMeses.get(month.getMonthOfYear()) + " "  + month.getYear());
		else
			showMonth = new JLabel(month.getDayOfMonth() + " de " + nombreMeses.get(month.getMonthOfYear()) + " de "  + month.getYear());
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		if(!selectedDay)
			head.add(new ShowUsersOverall(month),BorderLayout.SOUTH);
		else{
			MutableDateTime mdt = new MutableDateTime(month);
			mdt.setDayOfMonth(1);
			head.add(new ShowUsersOverall(new DateTime(mdt)),BorderLayout.SOUTH);
		}
		add(head,BorderLayout.NORTH);
		
		this.add(new ShowAuxsOverall(month,selectedDay),BorderLayout.CENTER);
		this.add(new SelectMonthPanel(this),BorderLayout.SOUTH);
		this.validate();
		this.repaint();
		this.owner.pack();
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
}
