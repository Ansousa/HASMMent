package es.uvigo.esei.hasmment.gui.overallview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import es.uvigo.esei.hasmment.dao.HibernateEntities;
import es.uvigo.esei.hasmment.dao.HibernateMethods;
import es.uvigo.esei.hasmment.entities.Asiste;
import es.uvigo.esei.hasmment.entities.Auxiliar;
import es.uvigo.esei.hasmment.entities.DBEntity;
import es.uvigo.esei.hasmment.entities.Usuario;
import es.uvigo.esei.hasmment.gui.detailview.ShowAuxDetail;

@SuppressWarnings("serial")
public class ShowAuxsOverall extends JPanel{
	private ArrayList<DBEntity> auxs, asists;
	boolean selectedDay;
	DateTime month;
	Interval intervalToShow, intervalForAuxs;
	
	JFreeChart chart;
	
	public ShowAuxsOverall(DateTime month, boolean selectedDay) {
		this.month = month;
		this.selectedDay = selectedDay;
		if(selectedDay){
			this.intervalToShow = new Interval(month.minus(month.getMinuteOfDay()*60*1000), month.minus(month.getMinuteOfDay()*60*1000).plusDays(1));
			this.intervalForAuxs = getInterval(new DateTime(month.getYear(), month.getMonthOfYear(),1,0,0));
		}
		else{
			this.intervalToShow = getInterval(month);
			this.intervalForAuxs = getInterval(month);
		}
		HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		HibernateMethods.getListEntities(HibernateEntities.PERMISO);
		auxs = HibernateMethods.getListEntities(HibernateEntities.AUXILIAR);
		initPanel();	
	}
	
	private Interval getInterval(DateTime month){
		DateTime start = new DateTime(month);
		DateTime end = new DateTime(month.plusMonths(1).minusDays(1));
		Interval i = new Interval(start, end);
		return i;
	}
	
	private void initPanel() {
		final CategoryDataset dataset = createDataset();
        /*final JFreeChart */chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        setBorder(new EmptyBorder(10, 20, 10, 20));
		setBackground(new Color(91, 106, 145));
        setLayout(new BorderLayout());
        add(chartPanel,BorderLayout.CENTER);
        
        chartPanel.addChartMouseListener(new ChartMouseListener() {
			
			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				String s = arg0.getEntity().toString().split(" ")[1].trim();
				if(s.split("=")[0].trim().equals("category") && s.split("=")[1].trim().length() == 9)
					new ShowAuxDetail(HibernateMethods.getAuxiliar(s.split("=")[1].trim()),month, selectedDay);
			}
		});
	}
	
	 private CategoryDataset createDataset() {
	        DefaultCategoryDataset result = new DefaultCategoryDataset();
	        float value=0;
			String rowKey="",columnKey="";
			HashMap<String, Float> map = new HashMap<String, Float>();
			for (DBEntity dbEntity : auxs) {
				Auxiliar a = (Auxiliar) dbEntity;
				map.put(a.getDni(), new Float(0)); //Usamos un map para saber cuantas horas hace cada auxiliar
			}
	        for (DBEntity dbEntity : asists) {
				Asiste a = (Asiste) dbEntity;
				if(intervalToShow.contains(a.getFechaHoraInicioAsistencia().getTime()) || intervalForAuxs.contains(a.getFechaHoraInicioAsistencia().getTime())) {
					
					value =(float) a.getFechaHoraFinAsistencia().getTime() - a.getFechaHoraInicioAsistencia().getTime();
					value = value/(1000*60*60);
					Usuario us = HibernateMethods.getUsuario(a.getDniUsuario());
					Auxiliar aux = HibernateMethods.getAuxiliar(a.getDniAuxiliar());
					rowKey = us.getDni() + " " + us.getNombre() + " " + us.getApellido1() + " " + us.getApellido2();
					columnKey = aux.getDni() + " " +  aux.getNombre() + " " + aux.getApellido1() + " " + aux.getApellido2();
					
					try{
						if(intervalToShow.contains(a.getFechaHoraInicioAsistencia().getTime())) {
							Number v = result.getValue(rowKey, columnKey);
							result.setValue(value + v.floatValue(), rowKey, columnKey);
						}
					}
					catch(Exception ex){
						if(intervalToShow.contains(a.getFechaHoraInicioAsistencia().getTime()))
							result.addValue(value, rowKey, columnKey);
					}
					finally {
						map.put(a.getDniAuxiliar(), map.get(a.getDniAuxiliar()) + value);
					}
				}
			}
	        for (DBEntity dbEntity : auxs) {
				Auxiliar a = (Auxiliar) dbEntity;
				result.addValue(a.getHoras() - map.get(a.getDni()), a.getNombre() + " " + a.getApellido1() + " " + a.getApellido2() , "Horas de holgura totales");
			}
	        
	        return result;
	    }
	    

	    private JFreeChart createChart(final CategoryDataset dataset) {
	        final JFreeChart chart = ChartFactory.createStackedBarChart(
	            "Horas Auxiliares",  // Encabezado del grafico
	            "Auxiliares (Click para ver en detalle) ",                  // Etiqueta de las Xs
	            "Horas",                     // Etiqueta del rango de las Xs
	            dataset,                     // Datos
	            PlotOrientation.HORIZONTAL,    // Orientacion
	            true,                        // legend
	            true,                        // tooltips
	            true                        // urls
	        );
	        chart.setBackgroundPaint(new Color(238, 238, 238));
	        
	        return chart;
	    }
}
