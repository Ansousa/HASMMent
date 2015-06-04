package es.uvigo.esei.hasmment.gui.overallview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
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

@SuppressWarnings("serial")
public class ShowAuxsOverall extends JPanel{
	private ArrayList<DBEntity> auxs, users, asists, pers;
	DateTime month;
	Interval intervalToShow;
	
	public ShowAuxsOverall(DateTime month) {
		this.month = month;
		this.intervalToShow = getInterval(month);
		users = HibernateMethods.getListEntities(HibernateEntities.USUARIO);
		asists = HibernateMethods.getListEntities(HibernateEntities.ASISTE);
		pers = HibernateMethods.getListEntities(HibernateEntities.PERMISO);
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
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        setBorder(new EmptyBorder(10, 20, 10, 20));
		setBackground(new Color(91, 106, 145));
        setLayout(new BorderLayout());
        add(chartPanel,BorderLayout.CENTER);
	}
	
	 private CategoryDataset createDataset() {
	        DefaultCategoryDataset result = new DefaultCategoryDataset();
	        Integer value;
			String rowKey,columnKey;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (DBEntity dbEntity : auxs) {
				Auxiliar a = (Auxiliar) dbEntity;
				map.put(a.getDni(), 0);
			}
	        for (DBEntity dbEntity : asists) {
				Asiste a = (Asiste) dbEntity;
				if(intervalToShow.contains(a.getFechaHoraInicioAsistencia().getTime())) {
					DateTime inicio = new DateTime(a.getFechaHoraInicioAsistencia());
					DateTime fin = new DateTime(a.getFechaHoraFinAsistencia());
					value = new Integer(fin.getMinuteOfDay()-inicio.getMinuteOfDay())/60;
					Interval i = new Interval(inicio.minus(new Integer(inicio.getMinuteOfDay())*60*1000),fin.minus(new Integer(fin.getMinuteOfDay())*60*1000));
					int h = map.get(a.getDniAuxiliar()) + value;
					int days = (int)(i.toDurationMillis()/(24*1000*60*60)+1);
					result.addValue(new Double(h*days), HibernateMethods.getUsuario(a.getDniUsuario()).getNombre(),HibernateMethods.getAuxiliar(a.getDniAuxiliar()).getNombre());
				}
			}
	        return result;
	    }
	    

	    private JFreeChart createChart(final CategoryDataset dataset) {

	        final JFreeChart chart = ChartFactory.createStackedBarChart(
	            "Horas Auxiliares",  // Encabezado del grafico
	            "Auxiliares",                  // Etiqueta de las Xs
	            "Horas",                     // Etiqueta del rango de las Xs
	            dataset,                     // Datos
	            PlotOrientation.HORIZONTAL,    // Orientacion
	            true,                        // legend
	            true,                        // tooltips
	            false                        // urls
	        );
	        chart.setBackgroundPaint(Color.white);
	        
	        
	        return chart;
	    }

	    private LegendItemCollection createLegendItems() {
	        LegendItemCollection result = new LegendItemCollection();
//	        LegendItem item1 = new LegendItem("US", new Color(0x22, 0x22, 0xFF));
	  //      LegendItem item2 = new LegendItem("Europe", new Color(0x22, 0xFF, 0x22));
	    //    LegendItem item3 = new LegendItem("Asia", new Color(0xFF, 0x22, 0x22));
	      //  LegendItem item4 = new LegendItem("Middle East", new Color(0xFF, 0xFF, 0x22));
//	        result.add(item1);
	  //      result.add(item2);
	    //    result.add(item3);
	      //  result.add(item4);
	        return result;
	    }
}
