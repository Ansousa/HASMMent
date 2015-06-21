package es.uvigo.esei.hasmment.gui.print;

import java.util.ArrayList;

public class UsuarioBeanList {
	ArrayList<UsuarioBean> list = new ArrayList<UsuarioBean>();
	
	public void addBean(String dia,	String horaInicio, String horaFin, String auxiliar) {
		UsuarioBean bean = new UsuarioBean();
		bean.setDia(dia);
		bean.setHoraInicio(horaInicio);
		bean.setHoraFin(horaFin);
		bean.setAuxiliar(auxiliar);
		list.add(bean);
	}
	
	public ArrayList<UsuarioBean> getList() {
		return list;
	}

}
