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

import com.mysql.jdbc.util.TimezoneDump;

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
		if(lastMonth.equals(new DateTime(1)))
			showMonth = new JLabel("Mes a mostrar: " + nombreMeses.get(lastMonth.getMonthOfYear()));
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		head.add(new ShowUsersOverall(lastMonth),BorderLayout.SOUTH);
		add(head,BorderLayout.NORTH);
		
		add(new ShowAuxsOverall(lastMonth),BorderLayout.CENTER);
		add(new SelectMonthPanel(this),BorderLayout.SOUTH);
	}
	
	public void updateMainContent() {
		this.removeAll();
		DateTime lastMonth = new DateTime(HibernateMethods.getLastDateAsist().getYear(), HibernateMethods.getLastDateAsist().getMonthOfYear(), 1, 0, 0);
		JLabel showMonth = new JLabel();
		JPanel head = new JPanel(new BorderLayout());
		JPanel show = new JPanel(new FlowLayout());
		if(lastMonth.equals(new DateTime(1)))
			showMonth = new JLabel("Mes a mostrar: " + nombreMeses.get(lastMonth.getMonthOfYear()));
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		head.add(new ShowUsersOverall(lastMonth),BorderLayout.SOUTH);
		add(head,BorderLayout.NORTH);
		
		this.add(new ShowAuxsOverall(lastMonth),BorderLayout.CENTER);
		this.add(new SelectMonthPanel(this),BorderLayout.SOUTH);
		this.validate();
		this.repaint();
	}
	
	public void updateMainContent(DateTime month) {
		this.removeAll();
		JPanel head = new JPanel(new BorderLayout());
		JPanel show = new JPanel(new FlowLayout());
		JLabel showMonth = new JLabel("Mes a mostrar: " + nombreMeses.get(month.getMonthOfYear()));
		showMonth.setFont(new Font("Arial", 1, 40));
		show.add(showMonth);
		head.add(show, BorderLayout.NORTH);
		head.add(new ShowUsersOverall(month),BorderLayout.SOUTH);
		add(head,BorderLayout.NORTH);
		
		this.add(new ShowAuxsOverall(month),BorderLayout.CENTER);
		this.add(new SelectMonthPanel(this),BorderLayout.SOUTH);
		this.validate();
		this.repaint();
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
