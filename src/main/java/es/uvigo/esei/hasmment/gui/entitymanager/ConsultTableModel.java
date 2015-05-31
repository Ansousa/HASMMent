package es.uvigo.esei.hasmment.gui.entitymanager;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ConsultTableModel extends AbstractTableModel{
	@SuppressWarnings("rawtypes")
	Vector<Vector> data;
	String columnNames[];
	
	@SuppressWarnings("rawtypes")
	public ConsultTableModel(String[] columnNames, Vector<Vector> rows) {
		this.columnNames = columnNames;
		this.data = rows;
	}
  
	@SuppressWarnings("rawtypes")
	public void updateRows(Vector<Vector> rows) {
		data = rows;
	}
  
	  public int getColumnCount() {
	    return columnNames.length;
	  }
	
	  public String getColumnName(int column) {
	    return columnNames[column];
	  }
	
	  public int getRowCount() {
	    return data.size();
	  }
	
	  public Object getValueAt(int row, int column) {
	    return data.get(row).get(column);
	  }
	
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int column) {
	    return (getValueAt(0, column).getClass());
	  }
	
	  @SuppressWarnings("unchecked")
	public void setValueAt(Object value, int row, int column) {
		  data.get(row).set(column, value);
	  }
	
	  public boolean isCellEditable(int row, int column) {
		  return (column != 0);
	  }
}
