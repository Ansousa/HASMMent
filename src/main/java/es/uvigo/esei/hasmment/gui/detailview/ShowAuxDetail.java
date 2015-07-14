package es.uvigo.esei.hasmment.gui.detailview;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;

@SuppressWarnings("serial")
public class ShowAuxDetail extends JFrame{
	private Auxiliar aux;
	private ArrayList<DBEntity> asists;
	private DateTime inicio, fin;
	private Interval interval;
	private String dayToShow;
	
	private HashMap<Integer, String> nombreMeses;
	
	public ShowAuxDetail(Auxiliar a, DateTime month, boolean oneDay) {
		this.aux = a;
		this.asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		if(!oneDay) {
			inicio = month;
			initNombreMeses();
			dayToShow = "Mes " + nombreMeses.get(month.getMonthOfYear()) + " " + month.getYear();
			fin = month.plusMonths(1);
			interval = new Interval(inicio,fin);
		}
		else{
			inicio = month.minusMinutes(month.getMinuteOfDay());
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			dayToShow = df.format(new Date(inicio.getMillis()));
			fin = inicio.plusDays(1);
			interval = new Interval(inicio,fin);
		}
		
		initFrame();
	}
	
	private void initFrame(){
		final IntervalCategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		
		setLayout(new BorderLayout());
		
		add(chartPanel,BorderLayout.CENTER);
			    
		setLocationRelativeTo(rootPane);
		setTitle("Detalles del Auxiliar " + this.aux.getDni() + " " + this.aux.getNombre() + " " + this.aux.getApellido1() + " " + this.aux.getApellido2());
		setVisible(true);
		pack();
	}
	
	private IntervalCategoryDataset createDataset() {
	    final TaskSeries s1 = new TaskSeries("Asistencia");
	    for (DBEntity dbEntity : asists) {
	    	Asiste a = (Asiste) dbEntity;
	    	if(a.getDniAuxiliar().equals(this.aux.getDni()) && interval.contains((a.getFechaHoraInicioAsistencia().getTime()))) {
	    		if(s1.get(HibernateMethods.getUsuario(a.getDniUsuario()).getNombre()) == null) {
	    			s1.add(new Task(HibernateMethods.getUsuario(a.getDniUsuario()).getNombre(), 
	    					new SimpleTimePeriod(new Date(a.getFechaHoraInicioAsistencia().getTime()), new Date(a.getFechaHoraFinAsistencia().getTime()))));
	    		}
	    		else {
	    			s1.get(HibernateMethods.getUsuario(a.getDniUsuario()).getNombre()).addSubtask(
	    					new Task("", 
	    	    					new SimpleTimePeriod(new Date(a.getFechaHoraInicioAsistencia().getTime()), new Date(a.getFechaHoraFinAsistencia().getTime()))));
	    		}
	    	}
	    }
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        return collection;
	}
	        
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            "Reparto de Asistencias",  // chart title
            "Usuario",              // domain axis label
            "Días",              // range axis label
            dataset,             // data
            false,                // include legend
            true,                // tooltips
            false                // urls
        );    
	        //chart.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);
        return chart;    
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
