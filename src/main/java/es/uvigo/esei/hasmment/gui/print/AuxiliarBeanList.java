package es.uvigo.esei.hasmment.gui.print;

import java.util.ArrayList;

public class AuxiliarBeanList {
	ArrayList<AuxiliarBean> list = new ArrayList<AuxiliarBean>();
	
	public void addBean(String dia,	String horaInicio, String horaFin, String usuario,String tarea) {
		AuxiliarBean bean = new AuxiliarBean();
		bean.setDia(dia);
		bean.setHoraInicio(horaInicio);
		bean.setHoraFin(horaFin);
		bean.setUsuario(usuario);
		bean.setTarea(tarea);
		list.add(bean);
	}
	
	public ArrayList<AuxiliarBean> getList() {
		return list;
	}
}
